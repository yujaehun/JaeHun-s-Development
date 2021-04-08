# Generated by Django 3.1.4 on 2020-12-21 11:50

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Item',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('seller_name', models.CharField(max_length=50)),
                ('item_name', models.CharField(max_length=50)),
                ('price', models.IntegerField()),
                ('immediate', models.IntegerField(default=0)),
                ('picture', models.ImageField(upload_to='%Y/%m/%d')),
                ('explain', models.CharField(default='None', max_length=500)),
                ('auction', models.IntegerField(default=0)),
                ('trading_place', models.CharField(default='None', max_length=500)),
                ('likes', models.IntegerField(default=0)),
                ('upload_item', models.DateTimeField()),
                ('end_time', models.DateTimeField()),
                ('sold_out', models.IntegerField(default=0)),
            ],
        ),
        migrations.CreateModel(
            name='MyUser',
            fields=[
                ('first_name', models.CharField(max_length=15)),
                ('last_name', models.CharField(max_length=15)),
                ('user_id', models.CharField(max_length=15, primary_key=True, serialize=False)),
                ('password', models.CharField(max_length=15)),
                ('email', models.CharField(max_length=15)),
                ('phone_number', models.CharField(max_length=15)),
                ('address', models.CharField(max_length=15)),
                ('ban_counter', models.IntegerField(default=0)),
            ],
        ),
        migrations.CreateModel(
            name='ShoppingList',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('item', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='flea_market.item')),
                ('myuser', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='flea_market.myuser')),
            ],
        ),
        migrations.CreateModel(
            name='Review',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('user_id', models.CharField(max_length=15)),
                ('review', models.CharField(max_length=400)),
                ('item', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='flea_market.item')),
            ],
        ),
        migrations.CreateModel(
            name='Report',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('title', models.CharField(max_length=50)),
                ('reported_user_id', models.CharField(max_length=15)),
                ('reported_item', models.CharField(max_length=15)),
                ('report_content', models.TextField()),
                ('picture', models.ImageField(upload_to='report')),
                ('myuser', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='flea_market.myuser')),
            ],
        ),
        migrations.CreateModel(
            name='PurchaseList',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('item', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='flea_market.item')),
                ('myuser', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='flea_market.myuser')),
            ],
        ),
        migrations.CreateModel(
            name='MyAuction',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('bidding', models.IntegerField()),
                ('item', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='flea_market.item')),
                ('myuser', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='flea_market.myuser')),
            ],
        ),
        migrations.CreateModel(
            name='Like',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('item', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='flea_market.item')),
                ('myuser', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='flea_market.myuser')),
            ],
        ),
        migrations.AddField(
            model_name='item',
            name='myuser',
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='flea_market.myuser'),
        ),
    ]
