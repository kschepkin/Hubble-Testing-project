@objects
    global-alert              xpath     .//h1[contains(@class,'content__title')]
    main-form                 xpath     .//div[@class='layout__cell layout__content']
    page-content-*            xpath     .//div[@class='layout__cell layout__content']/div
    contact-us-button         xpath     .//a[@data-statlog='contactUs']
    yandex-logo               xpath     .//a[@data-statlog='logo']

= Main section =
    @on desktop

        global-alert:
            text is "Ошибка 404. Нет такой страницы"

        main-form:
            image file  images/yandex404.png, error 20%
            count any page-content-* is 3

        page-content-1:
            inside main-form

        page-content-2:
            inside main-form
            below page-content-1

        page-content-3:
            inside main-form
            below page-content-2

        contact-us-button:
            inside page-content-1
            text is "напишите нам"

        yandex-logo:
             width 100 px
             height 43 px

