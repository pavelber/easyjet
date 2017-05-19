package org.easyjet.services

/**
 * Created by pavelb on 19/05/2017.
 */
interface LetterConsts {
    def letterSuffinx = """
        Мы почти никогда не ошибаемся!
        <br>
        А если Вы закажете дешевые билеты пожертвуйте 5-10 долларов на продолжение работы сервиса:


    <form action="https://www.paypal.com/cgi-bin/webscr" method="post" target="_top">
        <input type="hidden" name="cmd" value="_s-xclick">
        <input type="hidden" name="hosted_button_id" value="PDJ3MD7N62AXE">
        <input type="image" src="https://www.paypalobjects.com/en_US/i/btn/btn_donate_LG.gif" border="0" name="submit"
               alt="PayPal - The safer, easier way to pay online!">
        <img alt="" border="0" src="https://www.paypalobjects.com/en_US/i/scr/pixel.gif" width="1" height="1">
    </form>
    <p>
    <i>Ваша добрая фея</i>
    </p>

        """
}
