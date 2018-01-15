from django.shortcuts import render, HttpResponseRedirect
from authapp.forms import RegisterForm, LoginForm, EditForm
from django.contrib.auth.forms import PasswordChangeForm

from django.contrib import auth
from django.urls import reverse


def login(request):
    title = 'Вход'

    login_form = LoginForm(data=request.POST)

    if request.method == 'POST' and login_form.is_valid():
        username = request.POST['username']
        password = request.POST['password']
        user = auth.authenticate(username=username, password=password)
        if user and user.is_active:
            auth.login(request, user)
            return HttpResponseRedirect('/')

    content = {'title': title, 'login_form': login_form}

    return render(request, 'authapp/login.html', content)


def logout(request):
    auth.logout(request)
    return HttpResponseRedirect('/')


def register(request):
    title = 'Регистрация'
    if request.method == 'POST':
        register_form = RegisterForm(request.POST, request.FILES)

        if register_form.is_valid():
            register_form.save()
            return HttpResponseRedirect(reverse('auth:login'))
    else:
        register_form = RegisterForm()

    content = {'title': title, 'register_form': register_form}

    return render(request, 'authapp/register.html', content)


def edit(request):
    title = 'Редактирование'

    if request.method == 'POST':
        edit_form = EditForm(request.POST, request.FILES, instance=request.user)
        if edit_form.is_valid():
            edit_form.save()
            return HttpResponseRedirect(reverse('auth:edit'))
    else:
        edit_form = EditForm(instance=request.user)

    content = {'title': title, 'edit_form': edit_form}

    return render(request, 'authapp/edit.html', content)

def change_password(request):
    title = 'Изменение пароля'

    if request.method == 'POST':
        change_password_form = PasswordChangeForm(request.POST, instance=request.user)
        if change_password_form.is_valid():
            change_password_form.save()
            update_session_auth_hash(request, user) 
            messages.success(request, 'Пароль был успешно обновлен!')
            return HttpResponseRedirect(reverse('auth:change_password'))
        else:
            messages.error(request, 'Пожалуйста, исправьте ошибки ниже.')
    else:
        change_password_form = PasswordChangeForm(request.user)

    content = {'title': title, 'change_password_form': change_password_form}

    return render(request, 'authapp/change_password.html', content)