package com.edwin.github_app.network.entities

sealed class SubscriptionBody(val ignored: Boolean, val subscribed: Boolean)

object IGNORED: SubscriptionBody(true, false)

object  WATCH: SubscriptionBody(false, true)

//���￴��ȥ�� enum �ƺ����ã���ʵ���� enum �� Gson ���л� Json ��ʱ���ֱ�� toString����������Ҫ���� { "ignored": true, "subscribed": true}
//����ֱ�ӵģ�������ö�٣������Ҫ��ö�٣������Զ������л�����
//enum class SubscriptionBody(val ignored: Boolean, val subscribed: Boolean){
//    IGNORED(true, false), WATCH(false, true)
//}