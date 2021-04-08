from django import forms
from django.forms import ModelForm
from .models import Store

class Storephoto(ModelForm):
	class Meta:
		model = Store
		fields = ['myuser', 'store_name', 'pay', 'phone_number', 'store_content', 'job_type', 'location', 'photo']