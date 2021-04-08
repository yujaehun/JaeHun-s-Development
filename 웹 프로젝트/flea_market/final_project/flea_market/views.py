from django.http import HttpResponse, HttpResponseRedirect
from django.shortcuts import get_object_or_404, render
from django.urls import reverse
from django.contrib import auth
from django.contrib.auth.models import User
import os
from django.conf import settings
from datetime import datetime
from datetime import tzinfo,timedelta,datetime
from pytz import timezone

from .models import MyUser, Item, ShoppingList, Report, Review, MyAuction, PurchaseList, Like

def check_sold():

	items = Item.objects.all()
	auc = MyAuction.objects.values('id', 'item_id', 'myuser_id')
	for row in items:
		real_time = datetime.now();
		real_time = real_time.astimezone(timezone('Asia/Seoul'))
		if row.end_time < real_time:
			if row.auction:
				Item.objects.filter(id = row.id).update(sold_out = 1)
				for auct in auc:
					if auct['item_id'] == row.id:
						myauc = MyAuction.objects.get(pk = auct['id'])
						new_purchase = PurchaseList(myuser_id = auct['myuser_id'], item_id = auct['item_id'])
						new_purchase.save()
						myauc.delete()
	return

def homepage(request):
	check_sold()
	items = Item.objects.all().order_by('-id')
	if request.method == "POST":
		search = request.POST.get('search', False)
		search_product = request.POST.get('search_product', False)
		search_seller = request.POST.get('search_seller', False)
		price = request.POST.get('price', 0)
		auction = request.POST.get('auction', 0)
		if search_product or search_seller or price or auction:
			total_search = ""
			total_search += search_product + "@" + search_seller + "@" + str(price) + "@" + str(auction) + "@"
			return HttpResponseRedirect(reverse('flea_market:search_item', args=(total_search,)))
		elif search:
			return HttpResponseRedirect(reverse('flea_market:search_item', args=(search,)))
	
	user_id = request.user.username
	if user_id != "":
		user = MyUser.objects.get(pk = user_id)
		return render(request, 'homepage.html', {'name': user, 'items': items})
	return render(request, 'homepage.html', {'items': items})

def admin(request):
	check_sold()
	report = Report.objects.all().order_by('-id')
	return render(request, 'admin.html', {'reports': report})

def show_report(request, report_id):
	check_sold()
	report = Report.objects.get(pk = report_id)
	report_user = MyUser.objects.get(pk = report.myuser_id)
	reported_user = MyUser.objects.get(pk = report.reported_user_id)
	if request.method == "POST":
		report.delete()
		return HttpResponseRedirect(reverse('flea_market:admin'))
	return render(request, 'admin_report.html', {'report':report, 'report_user': report_user, 'reported_user': reported_user})

def show_user(request):
	check_sold()
	user = MyUser.objects.all()
	return render(request, 'admin_user.html', {'users': user})

def modify_user(request, user_id):
	check_sold()
	user = MyUser.objects.get(pk = user_id)
	if request.method == "POST":
		if request.POST.get('mod_btn', False):
			MyUser.objects.filter(user_id = user_id).update(user_id = request.POST.get('user_id', False), first_name = request.POST.get('first_name', False), last_name = request.POST.get('last_name',False), password = request.POST.get('password', False), email = request.POST.get('email', False), phone_number = request.POST.get('phone_number', False), address = request.POST.get('address', False), ban_counter = request.POST.get('ban_counter', False))
			return HttpResponseRedirect(reverse('flea_market:show_user'))
		if request.POST.get('del_btn', False):
			user.delete()
			u = User.objects.get(username = user_id)
			u.delete()
			return HttpResponseRedirect(reverse('flea_market:show_user'))

	return render(request, 'admin_modify.html', {'user': user})

def search_item(request, search):
	check_sold()
	user_id = request.user.username
	user = MyUser.objects.filter(user_id = user_id)
	product = []
	seller = []
	if "@" in search:
		search_ = search.split("@")
		search_product = search_[0]
		search_seller = search_[1]
		price = int(search_[2])
		if price == 91 or price == 0:
			price = 1000000000
		auction = int(search_[3])
		items = Item.objects.all().order_by('-id')
		for row in items:
			if int(row.price) <= price and row.sold_out == 0:
				if search_product != "" and search_seller != "":
					if search_product in row.item_name and search_seller in row.seller_name:
						if auction == row.auction:
							product.append(row)
				elif search_product != "" and search_product in row.item_name:
					if auction == row.auction:
						product.append(row)
				elif search_seller != "" and search_seller in row.seller_name:
					if auction == row.auction:
						seller.append(row)
				elif search_product == "" and search_seller == "":
					if auction == row.auction:
						seller.append(row)

	else:
		items = Item.objects.all().order_by('-id')
		if search == "None":
			product = items
		else:
			for row in items:
				if row.sold_out == 0:
					if search in row.item_name:
						product.append(row)
					elif search in row.seller_name:
						seller.append(row)
	if request.method == "POST":
		search = request.POST.get('search', False)
		search_product = request.POST.get('search_product', False)
		search_seller = request.POST.get('search_seller', False)
		price = request.POST.get('price', 0)
		auction = request.POST.get('auction', 0)
		if search_product or search_seller or price or auction:
			total_search = ""
			total_search += search_product + "@" + search_seller + "@" + str(price) + "@" + str(auction) + "@"
			return HttpResponseRedirect(reverse('flea_market:search_item', args=(total_search,)))
		elif search:
			return HttpResponseRedirect(reverse('flea_market:search_item', args=(search,)))
	
	return render(request, 'search.html', {'name': user, 'product': product, 'seller': seller})

def login(request):
	check_sold()
	auth.logout(request)
	if request.method == "POST":
		input_id = request.POST.get('inputID', False)
		input_pw = request.POST.get('inputPassword', False)
		if len(input_id) == 0 or len(input_pw) == 0:
			return render(request, 'login.html', {'msg': "Check your ID or Password"})
		users = MyUser.objects.values('user_id', 'password', 'ban_counter')
		for row in users:
			if row['user_id'] == input_id and row['password'] == input_pw:
				if row['ban_counter'] >= 5:
					return render(request, 'login.html', {'msg': "Your account is suspended"})
				user = auth.authenticate(request, username=input_id, password = input_pw)
				auth.login(request,user)
				if input_id == "admin":
					return HttpResponseRedirect(reverse('flea_market:admin'))
				return HttpResponseRedirect(reverse('flea_market:homepage'))
		return render(request, 'login.html', {'msg': "Check your ID or Password"})
	return render(request, 'login.html')

signup_id = False
def signup(request):
	check_sold()
	global signup_id
	if request.method == "POST":
		if request.POST.get('check_btn', False):
			signup_id = request.POST.get('username', False)
			if len(signup_id) == 0:
				signup_id = False
				return render(request, 'signup.html', {'msg': "Please input ID"})

			user = MyUser.objects.values('user_id')
			for row in user:
				if row['user_id'] == signup_id:
					signup_id = False
					return render(request, 'signup.html', {'msg': "This ID already exists"})
			return render(request, 'signup.html', {'signup_id':signup_id, 'msg': "You can use this ID"})
		else:
			if signup_id:
				if request.POST.get('password',False) == request.POST.get('confirm', False):
					phone = request.POST.get('phone', False)
					if len(phone) != 13 or phone[3] != "-" or phone[8] != "-":
						return render(request, 'signup.html', {'msg': "Please enter correct phone number"})
					new_user = MyUser(user_id = signup_id, first_name = request.POST.get('firstName', False), last_name = request.POST.get('lastName',False), password = request.POST.get('password', False), email = request.POST.get('email', False), phone_number = request.POST.get('phone', False), address = request.POST.get('address', False), ban_counter = 0)
					new_user.save()
					User.objects.create_user(username = new_user.user_id, password = new_user.password)
					signup_id = False
					return HttpResponseRedirect(reverse('flea_market:login'))
				return render(request, 'signup.html', {'msg': "Please confirm your password"})
			else:
				return render(request, 'signup.html', {'signup_id':signup_id, 'msg': "Please check your ID first"})
	return render(request, 'signup.html')


def purchase_list(request):
	check_sold()
	user_id = request.user.username
	user = MyUser.objects.get(pk = user_id)
	purchase_list = PurchaseList.objects.filter(myuser_id = user.user_id)

	if request.method == "POST":
		if request.POST.get('del_all_btn', False):
			purchase_list.delete()
		else:
			search = request.POST.get('search', False)
			search_product = request.POST.get('search_product', False)
			search_seller = request.POST.get('search_seller', False)
			price = request.POST.get('price', 0)
			auction = request.POST.get('auction', 0)
			if search_product or search_seller or price or auction:
				total_search = ""
				total_search += search_product + "@" + search_seller + "@" + str(price) + "@" + str(auction) + "@"
				return HttpResponseRedirect(reverse('flea_market:search_item', args=(total_search,)))
			elif search:
				return HttpResponseRedirect(reverse('flea_market:search_item', args=(search,)))
	my_list = []
	sum_price = 0
	for row in purchase_list:
		my_list.append(Item.objects.get(pk = row.item_id))
		sum_price += Item.objects.get(pk = row.item_id).price
	return render(request, 'purchase_list.html', {'name': user, 'list': my_list, 'sum': sum_price})

def wish_list(request):
	check_sold()
	user_id = request.user.username
	user = MyUser.objects.get(pk = user_id)
	if request.method == "POST":
		if request.POST.get('del_btn', False):
			delete_item = Like.objects.filter(item_id = request.POST.get('del_btn', False))
			like_item = Item.objects.get(pk = request.POST.get('del_btn', False))
			Item.objects.filter(id = like_item.id).update(likes = like_item.likes - 1)
			delete_item.delete()
		elif request.POST.get('del_all_btn', False):
			delete_item = Like.objects.filter(myuser_id = user.user_id)
			for row in delete_item:
				like_item = Item.objects.get(pk = row.item_id)
				Item.objects.filter(id = like_item.id).update(likes = like_item.likes - 1)
			delete_item.delete()
		else:
			search = request.POST.get('search', False)
			search_product = request.POST.get('search_product', False)
			search_seller = request.POST.get('search_seller', False)
			price = request.POST.get('price', 0)
			auction = request.POST.get('auction', 0)
			if search_product or search_seller or price or auction:
				total_search = ""
				total_search += search_product + "@" + search_seller + "@" + str(price) + "@" + str(auction) + "@"
				return HttpResponseRedirect(reverse('flea_market:search_item', args=(total_search,)))
			elif search:
				return HttpResponseRedirect(reverse('flea_market:search_item', args=(search,)))
	wish_list = Like.objects.filter(myuser_id = user.user_id)
	my_list = []
	sum_price = 0
	for row in wish_list:
		my_list.append(Item.objects.get(pk = row.item_id))
		sum_price += Item.objects.get(pk = row.item_id).price
	return render(request, 'wish_list.html', {'name': user, 'list': my_list, 'sum': sum_price})

def shopping_list(request):
	check_sold()
	user_id = request.user.username
	user = MyUser.objects.get(pk = user_id)
	if request.method == "POST":
		if request.POST.get('del_btn', False):
			delete_item = ShoppingList.objects.filter(item_id = request.POST.get('del_btn', False))
			delete_item.delete()
		elif request.POST.get('buy_all_btn', False):
			delete_item = ShoppingList.objects.filter(myuser_id = user.user_id)
			for row in delete_item:
				new_purchase = PurchaseList(myuser_id = row.myuser_id, item_id = row.item_id)
				new_purchase.save()
				Item.objects.filter(id = row.item_id).update(sold_out = 1)
			delete_item.delete()
		else:
			search = request.POST.get('search', False)
			search_product = request.POST.get('search_product', False)
			search_seller = request.POST.get('search_seller', False)
			price = request.POST.get('price', 0)
			auction = request.POST.get('auction', 0)
			if search_product or search_seller or price or auction:
				total_search = ""
				total_search += search_product + "@" + search_seller + "@" + str(price) + "@" + str(auction) + "@"
				return HttpResponseRedirect(reverse('flea_market:search_item', args=(total_search,)))
			elif search:
				return HttpResponseRedirect(reverse('flea_market:search_item', args=(search,)))
	shopping_list = ShoppingList.objects.filter(myuser_id = user.user_id)
	my_list = []
	sum_price = 0
	for row in shopping_list:
		my_list.append(Item.objects.get(pk = row.item_id))
		sum_price += Item.objects.get(pk = row.item_id).price
	return render(request, 'shopping_list.html', {'name': user, 'list': my_list, 'sum': sum_price})

def my_item(request):
	check_sold()
	user_id = request.user.username
	user = MyUser.objects.get(pk = user_id)
	item = Item.objects.filter(myuser_id = user.user_id)
	if request.method == "POST":
		if request.POST.get('del_btn', False):
			delete_item = Item.objects.get(pk = request.POST.get('del_btn', False))
			if delete_item.picture != "False":
				os.remove(os.path.join(settings.MEDIA_ROOT, delete_item.picture.path))
			delete_item.delete()
		elif request.POST.get('mod_btn', False):
			modify_item = Item.objects.get(pk = request.POST.get('mod_btn', False))
			return HttpResponseRedirect(reverse('flea_market:modify_item', args=(modify_item.id,)))
		elif request.POST.get('del_so_btn', False):
			Item.objects.filter(myuser_id = user_id, sold_out = 1).delete()
			return HttpResponseRedirect(reverse('flea_market:my_item'))
		elif request.POST.get('go_btn', False):
			return HttpResponseRedirect(reverse('flea_market:registration'))

		else:
			search = request.POST.get('search', False)
			search_product = request.POST.get('search_product', False)
			search_seller = request.POST.get('search_seller', False)
			price = request.POST.get('price', 0)
			auction = request.POST.get('auction', 0)
			if search_product or search_seller or price or auction:
				total_search = ""
				total_search += search_product + "@" + search_seller + "@" + str(price) + "@" + str(auction) + "@"
				return HttpResponseRedirect(reverse('flea_market:search_item', args=(total_search,)))
			elif search:
				return HttpResponseRedirect(reverse('flea_market:search_item', args=(search,)))
	return render(request, 'my_item.html', {'name': user, 'items': item})

def modify_item(request, item_id):
	check_sold()
	mod_item = Item.objects.filter(id = item_id)
	if len(mod_item) == 0:
		return HttpResponseRedirect(reverse('flea_market:homepage'))
	mod_item = Item.objects.get(pk = item_id)
	if request.method == "POST":
		if mod_item.picture != "False":
				os.remove(os.path.join(settings.MEDIA_ROOT, mod_item.picture.path))
		mod_item.delete()
		user_id = request.user.username
		user = MyUser.objects.get(pk = user_id)
		user_name = user.first_name + " " + user.last_name
		start_time = datetime.now()
		start_time = start_time.astimezone(timezone('Asia/Seoul'))
		end_time = start_time + timedelta(days = 1)
		end_time = end_time.astimezone(timezone('Asia/Seoul'))
		new_item = Item(myuser_id = user_id, seller_name = user_name, item_name = request.POST.get('itemname', False), price = request.POST.get('price', False), picture = request.FILES.get('picture', False), explain = request.POST.get('explain', False), auction = request.POST.get('auction', False), trading_place = request.POST.get('place', False), upload_item = start_time, end_time = end_time)
		new_item.save()
		return HttpResponseRedirect(reverse('flea_market:my_item'))
	return render(request, 'modify_item.html', {'item': mod_item})

def registration(request):
	check_sold()
	user_id = request.user.username
	user = MyUser.objects.get(pk = user_id)
	if request.method == "POST":
		user_name = user.first_name + " " + user.last_name
		start_time = datetime.now()
		end_time = start_time + timedelta(days = 1)
		start_time = start_time.astimezone(timezone('Asia/Seoul'))
		end_time = end_time.astimezone(timezone('Asia/Seoul'))
		new_item = Item(myuser_id = user_id, seller_name = user_name, item_name = request.POST.get('itemname', False), price = request.POST.get('price', False), picture = request.FILES.get('picture', False), explain = request.POST.get('explain', False), auction = request.POST.get('auction', False), trading_place = request.POST.get('place', False), immediate = request.POST.get('immediate', False), upload_item = start_time, end_time = end_time)
		new_item.save()
		return HttpResponseRedirect(reverse('flea_market:homepage'))
	return render(request, 'registration.html', {'name': user_id})

def item(request, item_id):
	check_sold()
	item = Item.objects.get(pk = item_id)	
	sellers_item = Item.objects.filter(seller_name = item.seller_name)
	user_id = request.user.username
	shopping_list = []
	item_review = Review.objects.filter(item_id = item_id)
	bid = MyAuction.objects.filter(item_id = item_id)
	bidder = ""
	end_time = item.end_time.astimezone(timezone('Asia/Seoul')).strftime('%Y-%m-%d %H:%M:%S')
	for row in bid:
		if row.item_id == item_id:
			bidder = row.myuser_id
	if user_id != "":
		user = MyUser.objects.get(pk = user_id)
		shopping_list = ShoppingList.objects.filter(myuser_id = user.user_id)
	if len(shopping_list) != 0:
		sh_list = "Already in shopping list"
	else :
		sh_list = ""
	seller = MyUser.objects.get(pk = item.myuser_id)
	
	if request.method == "POST" and request.POST.get('sl_btn', False):	
		if user_id == "":
			return HttpResponseRedirect(reverse('flea_market:login'))
		buy_item = ShoppingList.objects.filter(myuser_id = user_id)
		check = 0
		for row in buy_item:
			if row.myuser_id == user_id and row.item_id == item_id:
				check = 1
				break
		if not check:
			new_list = ShoppingList(myuser_id = user_id, item_id = item.id)
			new_list.save()
			return HttpResponseRedirect(reverse('flea_market:item', args=(item_id,)))
		else :
			return render(request, 'item.html', {'end_time':end_time, 'bidder':bidder, 'reviews':item_review, 'other_item': sellers_item, 'sh_list': sh_list, 'seller': seller, 'name': user_id, 'item': item, 'msg': "You already put it on the shopping list."})
	
	elif request.method == "POST" and request.POST.get('buy_btn', False):
		if user_id == "":
			return HttpResponseRedirect(reverse('flea_market:login'))
		buy_item = ShoppingList.objects.filter(myuser_id = user_id)
		new_purchase = PurchaseList(myuser_id = user_id, item_id = item_id)
		new_purchase.save()
		Item.objects.filter(id = item_id).update(sold_out = 1)
		for row in buy_item:
			if row.item_id == item_id:
				delete_item = ShoppingList.objects.get(pk = row.id)
				delete_item.delete()
		return HttpResponseRedirect(reverse('flea_market:item', args=(item_id,)))
	
	elif request.method == "POST" and request.POST.get('content', False):
		if user_id == "":
			return HttpResponseRedirect(reverse('flea_market:login'))
		reviews = Review.objects.values('user_id', 'item_id')
		for row in reviews:
			if row['user_id'] == user_id and row['item_id'] == item_id:
				return render(request, 'item.html', {'bidder':bidder, 'reviews':item_review, 'other_item': sellers_item, 'sh_list': sh_list, 'seller': seller, 'name': user_id, 'item': item, 'msg': "You already submit review."})
		new_review = Review(item_id = item_id, user_id = user_id, review = request.POST.get('content', False))
		new_review.save()
		return HttpResponseRedirect(reverse('flea_market:item', args=(item_id,)))
	
	elif request.method == "POST" and request.POST.get('bidding_btn', False):
		if user_id == "":
			return HttpResponseRedirect(reverse('flea_market:login'))
		if user_id == item.myuser_id:
			return render(request, 'item.html', {'end_time':end_time, 'reviews':item_review, 'other_item': sellers_item, 'sh_list': sh_list, 'seller': seller, 'name': user_id, 'item': item, 'msg': "You cannot participate in an auction in your product."})
		bidding_price = request.POST.get('bidding', False)
		if int(bidding_price) <= item.price:
			return render(request, 'item.html', {'end_time':end_time, 'reviews':item_review, 'other_item': sellers_item, 'sh_list': sh_list, 'seller': seller, 'name': user_id, 'item': item, 'msg': "Price is must higher than "+str(item.price)+" $"})
		if int(bidding_price) > item.immediate and item.immediate != 0:
			buy_item = MyAuction.objects.filter(myuser_id = user_id)
			Item.objects.filter(id = item_id).update(sold_out = 1)
			for row in buy_item:
				if row.item_id == item_id:
					delete_item = MyAuction.objects.get(pk = row.id)
					delete_item.delete()
			return HttpResponseRedirect(reverse('flea_market:item', args=(item_id,)))
		all_auction = MyAuction.objects.filter(myuser_id = user_id).filter(item_id = item_id)
		if len(all_auction) != 0:
			MyAuction.objects.filter(myuser_id = user_id).filter(item_id = item_id).update(bidding = int(bidding_price))
		else:
			new_auction = MyAuction(myuser_id = user_id, item_id = item_id, bidding = int(bidding_price))
			new_auction.save()

		Item.objects.filter(id = item_id).update(price = bidding_price)
		return HttpResponseRedirect(reverse('flea_market:item', args=(item_id,)))
	
	elif request.method == "POST" and request.POST.get('wish_btn', False):
		if user_id == "":
			return HttpResponseRedirect(reverse('flea_market:login'))
		buy_item = Like.objects.filter(myuser_id = user_id)
		check = 0
		for row in buy_item:
			if row.myuser_id == user_id and row.item_id == item_id:
				check = 1
				break
		if not check:
			new_list = Like(myuser_id = user_id, item_id = item.id)
			new_list.save()
			like_item = Item.objects.get(pk = item_id)
			Item.objects.filter(id = item_id).update(likes = like_item.likes + 1)
			return HttpResponseRedirect(reverse('flea_market:item', args=(item_id,)))
		else :
			return render(request, 'item.html', {'end_time':end_time, 'bidder':bidder, 'reviews':item_review, 'other_item': sellers_item, 'sh_list': sh_list, 'seller': seller, 'name': user_id, 'item': item, 'msg': "You already put it on the wish list."})

	elif request.method == "POST":
		search = request.POST.get('search', False)
		search_product = request.POST.get('search_product', False)
		search_seller = request.POST.get('search_seller', False)
		price = request.POST.get('price', 0)
		auction = request.POST.get('auction', 0)
		if search_product or search_seller or price or auction:
			total_search = ""
			total_search += search_product + "@" + search_seller + "@" + str(price) + "@" + str(auction) + "@"
			return HttpResponseRedirect(reverse('flea_market:search_item', args=(total_search,)))
		elif search:
			return HttpResponseRedirect(reverse('flea_market:search_item', args=(search,)))
	return render(request, 'item.html', {'end_time':end_time, 'bidder':bidder, 'reviews':item_review, 'other_item': sellers_item, 'sh_list': sh_list, 'seller': seller, 'name': user_id, 'item': item})

def report(request, item_id):
	check_sold()
	user_id = request.user.username
	user = MyUser.objects.get(pk = user_id)
	item = Item.objects.get(pk = item_id)
	if request.method == "POST":
		report = Report(myuser_id = user_id, reported_user_id = item.myuser_id, reported_item = item.id, report_content = request.POST.get('content', False), picture = request.FILES.get('picture', False), title = request.POST.get('title', False))
		report.save()
		return HttpResponseRedirect(reverse('flea_market:item', args=(item_id,)))
	return render(request, 'report.html', {'item': item})

def auction(request):
	check_sold()
	user_id = request.user.username
	user = MyUser.objects.get(pk = user_id)
	if request.method == "POST":
		search = request.POST.get('search', False)
		search_product = request.POST.get('search_product', False)
		search_seller = request.POST.get('search_seller', False)
		price = request.POST.get('price', 0)
		auction = request.POST.get('auction', 0)
		if search_product or search_seller or price or auction:
			total_search = ""
			total_search += search_product + "@" + search_seller + "@" + str(price) + "@" + str(auction) + "@"
			return HttpResponseRedirect(reverse('flea_market:search_item', args=(total_search,)))
		elif search:
			return HttpResponseRedirect(reverse('flea_market:search_item', args=(search,)))
	auction_list = MyAuction.objects.filter(myuser_id = user_id)
	my_list = []
	sum_price = 0
	for row in auction_list:
		if row.bidding < Item.objects.get(pk = row.item_id).price:
			del_auction = MyAuction.objects.get(pk = row.id)
			del_auction.delete()
		else:
			my_list.append(Item.objects.get(pk = row.item_id))
			sum_price += Item.objects.get(pk = row.item_id).price
	return render(request, 'auction.html', {'name':user_id, 'list': my_list, 'sum': sum_price})
# Create your views here.
