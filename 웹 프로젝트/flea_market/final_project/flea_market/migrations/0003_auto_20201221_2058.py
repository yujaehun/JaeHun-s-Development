# Generated by Django 3.1.4 on 2020-12-21 11:58

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('flea_market', '0002_auto_20201221_2055'),
    ]

    operations = [
        migrations.AlterField(
            model_name='myuser',
            name='email',
            field=models.CharField(max_length=30),
        ),
    ]
