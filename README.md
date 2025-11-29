# Проект автоматизации тестирования интернет-магазина CMT Science

> Автоматизированное тестирование функционала [магазина спортивного питания](https://cmtscience.ru/catalog) CMT Science.
> Проект охватывает ключевые пользовательские сценарии, включая работу с каталогом, корзиной, фильтрами и поиском.

## **Содержание:**

---

*   <a href="#tools">Технологии и инструменты</a>
*   <a href="#cases">Реализованные проверки</a>
*   <a href="#jenkins">Сборка в Jenkins</a>
*   <a href="#console">Запуск тестов</a>
*   <a href="#allure">Allure-отчет</a>
*   <a href="#allure-testops">Интеграция с Allure TestOps</a>
*   <a href="#jira">Интеграция с Jira</a>
*   <a href="#telegram">Уведомления в Telegram</a>
*   <a href="#video">Пример выполнения теста</a>

---

<a id="tools"></a>
## 🛠 **Технологии и инструменты:**

<p align="center">
  <a href="https://www.jetbrains.com/idea/"><img src="images/logo/Intelij_IDEA.svg" width="50" height="50" alt="IDEA"/></a>
  <a href="https://www.java.com/"><img src="images/logo/Java.svg" width="50" height="50" alt="Java"/></a>
  <a href="https://github.com/"><img src="images/logo/Github.svg" width="50" height="50" alt="Github"/></a>
  <a href="https://gradle.org/"><img src="images/logo/Gradle.svg" width="50" height="50" alt="Gradle"/></a>
  <a href="https://junit.org/junit5/"><img src="images/logo/JUnit5.svg" width="50" height="50" alt="JUnit 5"/></a>
  <a href="https://selenide.org/"><img src="images/logo/Selenide.svg" width="50" height="50" alt="Selenide"/></a>
  <a href="https://aerokube.com/selenoid/"><img src="images/logo/Selenoid.svg" width="50" height="50" alt="Selenoid"/></a>
  <a href="https://github.com/allure-framework/allure2"><img src="images/logo/Allure.svg" width="50" height="50" alt="Allure"/></a>
  <a href="https://qameta.io/"><img src="images/logo/Allure2.svg" width="50" height="50" alt="Allure TestOps"/></a>
  <a href="https://www.jenkins.io/"><img src="images/logo/Jenkins.svg" width="50" height="50" alt="Jenkins"/></a>
  <a href="https://www.atlassian.com/ru/software/jira/"><img src="images/logo/Jira.svg" width="50" height="50" alt="Jira"/></a>
  <a href="https://telegram.org/"><img src="images/logo/Telegram.svg" width="50" height="50" alt="Telegram"/></a>
</p>

*В проекте используются паттерны **Page Object** и **Page Elements** для структурированности и чистоты кода.*

---

<a id="cases"></a>
## ✅ **Реализованные проверки:**

**Тестирование каталога (`/catalog`):**
*   ✓ Проверка отображения и корректности работы фильтров (бренд, категория, цена)
*   ✓ Проверка сортировки товаров (по цене, популярности, новизне)
*   ✓ Проверка работы поиска в рамках каталога
*   ✓ Проверка корректности отображения карточек товаров (наличие изображения, названия, цены, кнопки "В корзину")
*   ✓ Проверка перехода на страницу товара из каталога

**Тестирование корзины:**
*   ✓ Добавление товара в корзину из каталога
*   ✓ Добавление товара в корзину со страницы товара
*   ✓ Изменение количества товаров в корзине
*   ✓ Удаление товара из корзины
*   ✓ Проверка расчета итоговой стоимости

**Общие проверки:**
*   ✓ Проверка работы основного меню и навигации
*   ✓ Проверка работы поиска по всему сайту
*   ✓ Проверка оформления заказа (позитивный и негативные сценарии)

---

<a id="jenkins"></a>
## <img alt="Jenkins" height="25" src="images/logo/Jenkins.svg" width="25"/> **Сборка в Jenkins:**

<p align="center">
<a href="[Ссылка на вашу джобу в Jenkins]"><img src="images/screen/jenkins_job.png" alt="Jenkins" width="950"/></a>
</p>

### **Параметры сборки:**

| Параметр | Значение по умолчанию | Описание |
|----------|-----------------------|----------|
| `browser` | `chrome` | Браузер для запуска тестов |
| `browserVersion` | `100.0` | Версия браузера |
| `browserSize` | `1920x1080` | Разрешение окна браузера |
| `baseUrl` | `https://cmtscience.ru` | Адрес тестового окружения |
| `remoteUrl` | `[Ваш Selenoid]` | Адрес удаленного сервера (Selenoid) |

---

<a id="console"></a>
## 🚀 **Запуск тестов:**

### **Локальный запуск:**
```bash
gradle clean test -DbaseUrl="https://cmtscience.ru"


### **Удаленный запуск (с параметрами из Jenkins):**
```bash
gradle clean test
-Dbrowser=${browser}
-DbrowserVersion=${browserVersion}
-DbrowserSize=${browserSize}
-DbaseUrl=${baseUrl}
-DremoteUrl=${remoteUrl}

<a id="allure"></a>

<img alt="Allure" height="25" src="images/logo/Allure.svg" width="25"/> Allure-отчет:
<p align="center"> <img title="Allure Overview Dashboard" src="images/screen/allure_overview.png" width="850"> </p>
Главная страница отчета:
Overview: Общая статистика по прогону (графики, диаграммы).

Suites: Группировка тестов по тестовым наборам.

Behaviors: Группировка по функциональным блокам (Epic, Feature, Story).

<p align="center"> <img title="Allure Tests" src="images/screen/allure_behaviors.png" width="850"> </p>

