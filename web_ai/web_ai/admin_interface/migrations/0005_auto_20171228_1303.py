# Generated by Django 2.0 on 2017-12-28 13:03

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('admin_interface', '0004_auto_20171222_1817'),
    ]

    operations = [
        migrations.RenameField(
            model_name='modelkeyword',
            old_name='person_id',
            new_name='person',
        ),
        migrations.AlterField(
            model_name='modelkeyword',
            name='keyword',
            field=models.CharField(max_length=2048),
        ),
    ]
