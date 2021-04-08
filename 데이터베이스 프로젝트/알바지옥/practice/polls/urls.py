from django.urls import path
from django.conf import settings
from django.conf.urls.static import static

from . import views

app_name = 'polls'
urlpatterns = [
	path('', views.home, name='home'),
	path('login/', views.login, name='login'),
	path('signup/', views.signup, name='signup'),
	path('<int:myuser_id>/', views.home_page, name='home_page'),
	path('<int:myuser_id>/admin/', views.admin_page, name='admin_page'),
	path('<int:myuser_id>/admin/<int:report_id>/', views.admin_report, name='admin_report'),
	path('<int:myuser_id>/store/', views.post_store, name='post_store'),
	path('<int:myuser_id>/<int:store_id>/', views.select_store, name='select_store'),
	path('<int:myuser_id>/<int:store_id>/resume/', views.resume, name = 'resume'),
	path('<int:myuser_id>/mystore/', views.mystore, name='mystore'),
	path('<int:myuser_id>/mystore/<int:store_id>/', views.mystore_resume, name = 'mystore_resume'),
	path('<int:myuser_id>/mystore/<int:store_id>/<int:resume_id>', views.submitted_resume, name = 'submitted_resume'),
	path('<int:myuser_id>/myresume/', views.myresume, name = 'myresume'),
	path('<int:myuser_id>/myresume/<int:resume_id>/', views.myresume_detail, name = 'myresume_detail'),
	path('<int:myuser_id>/<int:store_id>/report/', views.report, name = 'report'),
	path('<int:myuser_id>/mystore/<int:store_id>/<int:resume_id>/report/', views.user_report, name = 'user_report')
]+static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)