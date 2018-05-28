#	OPIS DZIAŁANIA APLIKACJI I INSTRUKCJA OBSŁUGI
Aplikacja składa się z szeregu komponentów, które odpowiadają za poszczególne aspekty jej działania. W rozdziale 4 zaprezentowano najważniejsze z nich.


##	 Ogólna zasada działania

Cały projekt bazuje na komunikacji z serwerem – request i response.
 
##	 Transfer danych

W projekcie użyto DTO, czyli Data Transfer Object. Jest wzorcem projektowym należącym do grupy wzorców dystrybucji. Podstawowym zadaniem DTO jest transfer danych pomiędzy systemami, aplikacjami lub też w ramach aplikacji pomiędzy warstwami aplikacji. Należy wspomnieć o 3 głównych klasach: "Purchase", "Product" oraz "User".
Klasy te posiadają pola je identyfikujące. Jednak najważniejszym polem, które jest unikalne dla każdej z nich, jest „id”.

##	 Logowanie i „Mój profil”

Zaraz po uruchomieniu programu, użytkownikowi ukazuje się ekran startowy. Dzieli się on na :
- część boczną, gdzie znajdują się główne przyciski, odpowiedziale za nawigację po całym sklepie, 
- część górną, która przedstawia logo sklepu, oraz panel logowania,
- część centralną, gdzie w zależności od wyboru opcji w części bocznej, pokazywać będą sie kolejne widoki.

Po poprawnym zalogowaniu, użytkownikowi ukazuje się panel transakcji. Widnieją w nim zakupy, których dokonał, wraz z niezbędnymi informacjami na ich temat.

Tutaj użytkownik może dokonywać oceny zakupionego przedmiotu (w skali od 1 do 5, a każdy przedmiot można ocenić dokładnie 1 raz), czy też generować faktury. Z tego miejsca, użytkownik może przejść również do panelu zarządzania danymi, gdzie istnieje możlwość ich edycji oraz doładowania stanu konta.

##	 Rejestracja nowego użytkownika

Wówczas, gdy użytkownik nie posiada konta w sklepie, nalezy je założyć.  Po kliknięciu w przycisk  „rejestracja”,w części górnej ekranu startowego, ukazuje się panel rejestracji. Wystarczy wypęłnić podane pola i kliknąć przycisk „zarejestruj”. Od teraz użytkownik posiada własne konto, na które może się zalogować. 

##	 Produkty

Istnieją 3 typy produktów:
- AGD, czyli przedmioty typu AGD,
- MOBILES - telefony,
- WEAR - ubrania.

Ich przegląd determinują przyciski panelu bocznego. 

Użytkownik posiada pełen komfort przeglądania. Może wejść w widok szczegółów, gdzie widnieją bardziej szczegółowe informacje o danym produkcie.
W tej części należy również wspomnieć  o produktach „TOP”. Jest to zestawienie produktów najczęściej kupowanych oraz najlepiej ocenianych.

##	 Algorytm kupowania

Po wejściu w  widok listy produktów, można wybrać produkt a następnie nacisnąć przycisk „Kup”. Użytkownik zostanie poinformowany o tym, że przedmiot został dodany do koszyka, komunikatem.

Po przejściu do koszyka, ukazany zostaje produkt, oraz stan konta wraz z informacją, czy budżet klienta pozwala na kupno. Kolor zielony oznacza, iż użytkownik posiada wystarczające środki na koncie.

Po dokonaniu zakupu, przemiot znajdzie się w panelu „Mój profil”, który został  opisany wcześniej. Będzie można go tam  ocenić.
Ważną informacją jest, że kupić można tylko, będąc zalogowanym. 


##	 Walidacje i zabezpieczenia aplikacji

Program posiada szereg alertów, które pęłnią rolę zabezpieczeń przed różnymi sytuacjami, takimi jak podanie złych danych, pustych pól, czy kupno przedmiotu bez zalogowania. Poniżej przedstawiono niektóre, najważniejsze z nich:

- podanie złych danych, 
- podanie pustych pól,
- ocena już ocenionego przedmiotu.

