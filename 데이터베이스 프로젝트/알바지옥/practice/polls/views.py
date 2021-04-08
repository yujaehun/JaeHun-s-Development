from django.http import HttpResponse, HttpResponseRedirect
from django.shortcuts import get_object_or_404, render
from django.urls import reverse
from django.contrib import auth
from django.contrib.auth.models import User

from .models import Ban, Report, MyUser, Store, Resume

def home(request):
	auth.logout(request)
	return render(request, 'polls/home.html')

def signup(request):
	if request.method == "POST":
		if request.POST["password1"] == request.POST["password2"]:
			new_user = MyUser(name = request.POST['username'], nickname = request.POST['nickname'], password = request.POST['password1'], birth = request.POST['birth'], phone_number = request.POST['phonenumber'])
			new_user.save()
			ban = Ban(myuser_id = new_user.id)
			ban.save()
			user = User.objects.create_user(id = new_user.id, username=request.POST['nickname'], password = request.POST['password1'])
			return HttpResponseRedirect(reverse('polls:login'))
		return render(request, 'polls/signup.html')
	return render(request, 'polls/signup.html')

def login(request):
	if request.method == "POST":
		input_id = request.POST['username']
		input_pw = request.POST['password']
		user = MyUser.objects.values('id', 'name', 'nickname', 'password', 'ban_counter')
		for row in user:
			if(row['nickname'] == input_id):
				if(row['password'] == input_pw):
					if(row['id'] == 1):
						return HttpResponseRedirect(reverse('polls:admin_page', args=(row['id'],)))
					else:
						ban = Ban.objects.get(pk = row['id'])
						MyUser.objects.filter(id = row['id']).update(ban_counter = ban.ban_counter)
						login_user = auth.authenticate(request, username =input_id, password = input_pw)
						auth.login(request, login_user)
						return HttpResponseRedirect(reverse('polls:home_page', args=(row['id'],)))
		return render(request, 'polls/login.html', {'error': "wrong id or password"})
	return render(request, 'polls/login.html')

def admin_page(request, myuser_id):
	ban = Ban.objects.all();
	report = Report.objects.all();
	return render(request, 'polls/admin_page.html', {'user': myuser_id, 'report': report, 'ban': ban})

def admin_report(request, myuser_id, report_id):
	report = Report.objects.get(pk = report_id)
	reported_user = MyUser.objects.get(pk = report.reported_user_id)
	ban = Ban.objects.get(pk = reported_user.id)
	if request.method == "POST":
		Ban.objects.filter(myuser_id = reported_user.id).update(ban_counter = request.POST['ban_counter'])
		new_ban = Ban.objects.get(pk = reported_user.id)
		if(new_ban.ban_counter > 5):
			reported_user.delete();
			report.delete();
		return HttpResponseRedirect(reverse('polls:admin_page', args=(myuser_id,)))
	return render(request, 'polls/admin_report.html', {'reported_user': reported_user, 'report': report, 'ban': ban})

def home_page(request, myuser_id):
	user = MyUser.objects.get(pk=myuser_id)
	store = Store.objects.all()
	return render(request, 'polls/home_page.html', {'user': user, 'store': store})

def post_store(request, myuser_id):
	if request.method == "POST":
		new_store = Store(myuser_id = myuser_id, store_name = request.POST['store_name'], pay = request.POST['pay'], phone_number = request.POST['phone_number'], store_content = request.POST['store_content'], job_type = request.POST['job_type'], location = request.POST['location'])
		new_store.save()
		return HttpResponseRedirect(reverse('polls:home_page', args=(myuser_id,)))
	return render(request, 'polls/post_store.html', {'user': myuser_id})

def select_store(request, myuser_id, store_id):
	store = Store.objects.get(pk=store_id)
	return render(request, 'polls/select_store.html', {'user': myuser_id, 'store': store})

def resume(request, myuser_id, store_id):
	myuser = MyUser.objects.get(pk = myuser_id)
	if request.method == "POST":
		store = Store.objects.get(pk=store_id)
		new_resume = Resume(myuser_id = myuser_id, store_id = store_id, store_name = store.store_name, name = request.POST['name'], birth = request.POST['birth'], phone_number = request.POST['phone_number'], resume_content = request.POST['resume_content'])
		new_resume.save()
		return HttpResponseRedirect(reverse('polls:home_page', args=(myuser_id,)))
	return render(request, 'polls/resume.html', {'user': myuser_id, 'myuser': myuser})

def mystore(request, myuser_id):
	store = Store.objects.filter(myuser_id = myuser_id)
	if request.method == "POST":
		for row in store:
			if request.POST['delete'] == row.store_name:
				delete_store = Store.objects.get(pk = row.id)
				delete_store.delete()
				store = Store.objects.filter(myuser_id = myuser_id)
				return render(request, 'polls/mystore.html', {'user': myuser_id, 'store': store})
	return render(request, 'polls/mystore.html', {'user': myuser_id, 'store': store})

def mystore_resume(request, myuser_id, store_id):
	resume = Resume.objects.filter(store_id = store_id)
	return render(request, 'polls/mystore_resume.html', {'user': myuser_id, 'store': store_id, 'resume': resume})

def submitted_resume(request, myuser_id, store_id, resume_id):
	resume = Resume.objects.get(pk = resume_id)
	return render(request, 'polls/submitted_resume.html', {'user': myuser_id, 'store': store_id,'resume': resume})

def myresume(request, myuser_id):
	myresume = Resume.objects.filter(myuser_id=myuser_id)
	return render(request, 'polls/myresume.html', {'user': myuser_id, 'myresume': myresume})

def myresume_detail(request, myuser_id, resume_id):
	resume = Resume.objects.get(pk = resume_id)
	if request.method == "POST":
		resume.delete()
		return HttpResponseRedirect(reverse('polls:myresume', args=(myuser_id,)))
	return render(request, 'polls/myresume_detail.html', {'resume': resume})


def report(request, myuser_id, store_id):
	user = MyUser.objects.get(pk = myuser_id)
	store = Store.objects.get(pk = store_id)
	if request.method == "POST":
		new_report = Report(myuser_id = myuser_id, reported_user_id = store.myuser_id, report_content = request.POST['report_content'])
		new_report.save()
		return HttpResponseRedirect(reverse('polls:select_store', args=(myuser_id, store_id,)))
	return render(request, 'polls/report.html', {'user': user, 'store': store})

def user_report(request, myuser_id, store_id, resume_id):
	user = MyUser.objects.get(pk = myuser_id)
	store = Store.objects.get(pk = store_id)
	resume = Resume.objects.get(pk = resume_id);
	if request.method == "POST":
		new_report = Report(myuser_id = myuser_id, reported_user_id = resume.myuser_id, report_content = request.POST['report_content'])
		new_report.save()
		return HttpResponseRedirect(reverse('polls:mystore_resume', args=(myuser_id, store_id,)))
	return render(request, 'polls/user_report.html', {'user': user, 'resume': resume})

# Create your views here.
