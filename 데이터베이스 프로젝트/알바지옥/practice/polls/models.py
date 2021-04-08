from django.db import models

class MyUser(models.Model):	
	name = models.CharField(max_length= 15)
	nickname = models.CharField(max_length= 15)
	password = models.CharField(max_length = 15)
	birth = models.CharField(max_length = 15)
	phone_number = models.CharField(max_length=15)
	ban_counter = models.IntegerField(default=0)

class Ban(models.Model):
	myuser_id = models.IntegerField(default = 0, primary_key = True)
	ban_counter = models.IntegerField(default = 0)

class Report(models.Model):
	myuser = models.ForeignKey(MyUser, on_delete=models.CASCADE)
	reported_user_id = models.CharField(max_length = 15)
	report_content = models.TextField()

class Store(models.Model):
	myuser = models.ForeignKey(MyUser, on_delete=models.CASCADE)
	store_name = models.CharField(max_length = 50)
	pay = models.CharField(max_length = 20)
	phone_number = models.CharField(max_length = 15)
	store_content = models.TextField()
	job_type = models.CharField(max_length = 15)
	location = models.CharField(max_length = 30)

class Resume(models.Model):
	myuser = models.ForeignKey(MyUser, on_delete=models.CASCADE)
	store = models.ForeignKey(Store, on_delete=models.CASCADE)
	store_name = models.CharField(max_length = 50)
	name = models.CharField(max_length = 15)
	birth = models.CharField(max_length = 15)
	phone_number = models.CharField(max_length = 15)
	resume_content = models.TextField()


# Create your models here.
