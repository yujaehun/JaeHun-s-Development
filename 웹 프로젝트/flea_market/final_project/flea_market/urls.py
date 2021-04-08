from django.urls import path
from django.conf import settings
from django.conf.urls.static import static

from . import views
app_name = 'flea_market'
urlpatterns = [
	path('', views.homepage, name = 'homepage'),
	path('admin/', views.admin, name = 'admin'),
	path('login/', views.login, name = 'login'),
	path('signup/', views.signup, name = 'signup'),
	path('purchase_list/', views.purchase_list, name = 'purchase_list'),
	path('shopping_list/', views.shopping_list, name = 'shopping_list'),
	path('wish_list/', views.wish_list, name = 'wish_list'),
	path('auction/', views.auction, name = 'auction'),
	path('my_item/', views.my_item, name = 'my_item'),
	path('my_item/<int:item_id>/', views.modify_item, name = 'modify_item'),
	path('registration/', views.registration, name = 'registration'),
	path('<int:item_id>/', views.item, name = 'item'),
	path('<str:search>/', views.search_item, name = 'search_item'),
	path('<int:item_id>/report/', views.report, name = 'report'),
	path('admin/<int:report_id>/', views.show_report, name = 'show_report'),
	path('admin/user/', views.show_user, name = 'show_user'),
	path('admin/user/<str:user_id>/', views.modify_user, name = 'modify_user'),


]+static(settings.STATIC_URL, document_root=settings.STATIC_ROOT)

urlpatterns += static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)
