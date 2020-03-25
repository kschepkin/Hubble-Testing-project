@objects
    global-alert              xpath     .//div[@data-qa='message-error']
    main-form                 xpath     .//div[contains(@class,'page-error')]
    page-content-*            xpath     .//div[contains(@class,'page-error')]/p
    support-email             xpath     .//a[@class='page-error__email-address']
    goToMain-button           xpath     .//a[@href='/']

= Main section =
    @on desktop

        global-alert:
            text is "404 Страница не найдена"

        main-form:
            image file  images/error404.png, error 10%
            count any page-content-* is 4

        page-content-1:
            inside main-form
            text is "404"

        page-content-2:
            inside main-form
            below page-content-1
            text lowercase is "страница не найдена"

        page-content-3:
            inside main-form
            below page-content-2

        support-email:
            inside page-content-4
            text is "site@gloria-jeans.ru"

        goToMain-button:
            inside main-form
            below page-content-4
            text lowercase is "вернуться на главную"