from django.contrib import admin

from .models import MyUser, Report, Store, Resume, Ban

admin.site.register(MyUser)
admin.site.register(Report)
admin.site.register(Store)
admin.site.register(Resume)
admin.site.register(Ban)

# Register your models here.
