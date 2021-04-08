from django.db import models
import datetime

class MyUser(models.Model):	
	first_name = models.CharField(max_length= 15)
	last_name = models.CharField(max_length= 15)
	user_id = models.CharField(max_length= 15, primary_key=True)
	password = models.CharField(max_length = 15)
	email = models.CharField(max_length = 30)
	phone_number = models.CharField(max_length=15)
	address = models.CharField(max_length=100)
	ban_counter = models.IntegerField(default=0)

class Item(models.Model):
	myuser = models.ForeignKey(MyUser, on_delete=models.CASCADE)
	seller_name = models.CharField(max_length = 50)
	item_name = models.CharField(max_length = 50)
	price = models.IntegerField()
	immediate = models.IntegerField(default = 0)
	picture = models.ImageField(upload_to = "%Y/%m/%d")
	explain = models.CharField(max_length = 500, default = "None")
	auction = models.IntegerField(default = 0)
	trading_place = models.CharField(max_length = 500, default = "None")
	likes = models.IntegerField(default = 0)
	upload_item = models.DateTimeField()
	end_time = models.DateTimeField()
	sold_out = models.IntegerField(default = 0)

class Review(models.Model):
	item = models.ForeignKey(Item, on_delete=models.CASCADE)
	user_id = models.CharField(max_length = 15)
	review = models.CharField(max_length = 400)

class ShoppingList(models.Model):
	myuser = models.ForeignKey(MyUser, on_delete=models.CASCADE)
	item = models.ForeignKey(Item, on_delete=models.CASCADE)

class Like(models.Model):
	myuser = models.ForeignKey(MyUser, on_delete=models.CASCADE)
	item = models.ForeignKey(Item, on_delete=models.CASCADE)

class Report(models.Model):
	myuser = models.ForeignKey(MyUser, on_delete=models.CASCADE)
	title = models.CharField(max_length = 50)
	reported_user_id = models.CharField(max_length = 15)
	reported_item = models.CharField(max_length = 15)
	report_content = models.TextField()
	picture = models.ImageField(upload_to = "report")

class MyAuction(models.Model):
	myuser = models.ForeignKey(MyUser, on_delete=models.CASCADE)
	item = models.ForeignKey(Item, on_delete=models.CASCADE)
	bidding = models.IntegerField()

class PurchaseList(models.Model):
	myuser = models.ForeignKey(MyUser, on_delete=models.CASCADE)
	item = models.ForeignKey(Item, on_delete=models.CASCADE)

# Create your models here.
