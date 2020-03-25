@objects
    main-form                 xpath     .//div[contains(@class,'wrapper-header')]
    header-line               xpath     .//div[@class='header-location']
    header-line-item-*        xpath     .//div[@class='header-location']/div
    region-name               xpath     .//span[contains(@class,'js-name-region')]
    search-input              xpath     .//input[@id='js-site-search-input']
    auth-button               xpath     .//span[@data-qa='account-link']
    heart-icon                xpath     .//span[contains(@class,'icon--heart')]
    cart-icon                 xpath     .//div[@class="header-functions"]//*[@data-qa="icon-cart"]//*[contains(@class,'icon')]
    nav-block                 xpath     .//nav[@class='navigation']
    nav-block-item-*          xpath     .//div[@class='navigation-list desktop-show']/a
    nav-block-hover-active    xpath     .//div[contains(@class,'header-dropdown-content')][contains(@class,'active')]
    nav-dropdown-content      xpath     .//div[contains(@class,'header-dropdown-content')][contains(@class,'active')]//div[@class="header-dropdown-content__item"]

= Main section =
    @on desktop
        main-form:
            image file  images/header-desktop.png, error 5%

        header-line:
            inside main-form
            count any header-line-item-* is 4

        region-name:
            inside header-line


        search-input :
          inside main-form
          below header-line 30 to 35 px

        auth-button :
          inside main-form
          text is "Войти"

        heart-icon :
          inside main-form
          left-of cart-icon 24 px
          right-of auth-button 24 px
          width 30 px
          height 30 px

        cart-icon :
          inside main-form
          near heart-icon 24 px right
          width 30 px
          height 30 px

        nav-block :
            inside main-form
            count any nav-block-item-* is 5

        nav-dropdown-content :
          visible
          near main-form 15 to 25 px bottom
