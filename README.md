##	OPIS DZIAŁANIA APLIKACJI I INSTRUKCJA OBSŁUGI
Aplikacja składa się z szeregu komponentów, które odpowiadają za poszczególne aspekty jej działania. W rozdziale 4 zaprezentowano najważniejsze z nich.


#	 Ogólna zasada działania

Cały projekt bazuje na komunikacji z serwerem – request i response.
Rys. 1 przedstawia klasę „request”, natomiast rys. 2, klasę „response”.

 
Rys. 1 – widok typu wyliczeniowego „request_id”

 
Rys. 2 – widok typu wyliczeniowego „response”
4.2.	 Transfer danych

W projekcie użyto DTO, czyli Data Transfer Object. Jest wzorcem projektowym należącym do grupy wzorców dystrybucji. Podstawowym zadaniem DTO jest transfer danych pomiędzy systemami, aplikacjami lub też w ramach aplikacji pomiędzy warstwami aplikacji. Rys. 3, 4 i 5 kolejno obrazują kod 3 klas : „Product”, „Purchase” oraz „User”.
Klasy te posiadają pola je identyfikujące. Jednak najważniejszym polem, które jest unikalne dla każdej z nich, jest „id”.

 
Rys. 3 – widok klasy „Product”


 
Rys. 4 – widok klasy „Purchase”





 
Rys. 5 – widok klasy „User”

4.3.	 Logowanie i „Mój profil”

Zaraz po uruchomieniu programu, użytkownikowi ukazuje się ekran startowy. Dzieli się on na :
- część boczną, gdzie znajdują się główne przyciski, odpowiedziale za nawigację po całym sklepie, 
- część górną, która przedstawia logo sklepu, oraz panel logowania,
- część centralną, gdzie w zależności od wyboru opcji w części bocznej, pokazywać będą sie kolejne widoki.
  Na rys. 6 znajduje się zrzut ekranu, ilustrujący stan programu, zaraz po uruchomieniu.

 
Rys. 6 – widok ekranu startowego

Po poprawnym zalogowaniu, użytkownikowi ukazuje się panel transakcji. Widnieją w nim zakupy, których dokonał, wraz z niezbędnymi informacjami na ich temat, co obrazuje rys. 7.

 
Rys. 7 – widok panelu transakcji

Tutaj użytkownik może dokonywać oceny zakupionego przedmiotu (w skali od 1 do 5, a każdy przedmiot można ocenić dokładnie 1 raz), czy też generować faktury. Z tego miejsca, użytkownik może przejść również do panelu zarządzania danymi, który został przedstawiony na rys. 8,  gdzie istnieje możlwość ich edycji oraz doładowania stanu konta.
 
Rys. 8 – widok panelu edycji danych



4.4.	 Rejestracja nowego użytkownika

Wówczas, gdy użytkownik nie posiada konta w sklepie, nalezy je założyć.  Po kliknięciu w przycisk  „rejestracja”,w części górnej ekranu startowego, ukazuje się panel rejestracji. Wystarczy wypęłnić podane pola i kliknąć przycisk „zarejestruj”. Od teraz użytkownik posiada własne konto, na które może się zalogować.  Rys. 9 ilustruje panel panel rejestracji.

 
Rys. 9 – widok panelu rejestracji nowego użytkownika


4.5.	 Produkty

Istnieją 3 typy produktów:
- AGD, czyli przedmioty typu AGD,
- MOBILES - telefony,
-WEAR -  ubrania.

Ich przegląd determinują przyciski panelu bocznego. Na rys. 10, został przedstawiony widok listy produktów, po wejściu w przedmioty typu AGD.






 
Rys. 10 – widok przedmiotów „AGD”

Użytkownik posiada pełen komfort przeglądania. Może wejść w widok szczegółów, gdzie widnieją bardziej szczegółowe informacje o danym produkcie, co obrazuje rys. 11.

 
Rys. 11 – widok szczegółów produktu
W tej części należy również wspomnieć  o produktach „TOP”. Jest to zestawienie produktów najczęściej kupowanych oraz najlepiej ocenianych. Widok ten ilustruje rys. 12.

 
Rys. 12 – widok panelu „TOP”

4.6.	 Algorytm kupowania

Po wejściu w  widok listy produktów, można wybrać produkt a następnie nacisnąć przycisk „Kup”. Użytkownik zostanie poinformowany o tym, że przedmiot został dodany do koszyka, komunikatem, który został przedstawiony na rys. 13.

 
Rys. 13 – komunikat kupna produktu

Po przejściu do koszyka, ukazany zostaje produkt, oraz stan konta wraz z informacją, czy budżet klienta pozwala na kupno. Kolor zielony oznacza, iż użytkownik posiada wystarczające środki na koncie. Zostało do pokazane na rys. 14.

 
Rys. 14 – widok koszyka

Po dokonaniu zakupu, przemiot znajdzie się w panelu „Mój profil”, który został  opisany wcześniej. Będzie można go tam  ocenić.
Ważną informacją jest, że kupić można tylko, będąc zalogowanym. 

4.7.	 Walidacje i zabezpieczenia aplikacji

Program posiada szereg alertów, które pęłnią rolę zabezpieczeń przed różnymi sytuacjami, takimi jak podanie złych danych, pustych pól, czy kupno przedmiotu bez zalogowania. Poniżej przedstawiono niektóre, najważniejsze z nich:

- podanie złych danych, rys. 15:

 
Rys. 15 – widok obrazujący podanie złych danych


- podanie pustych pól, rys. 16:

 
Rys. 16 – widok obrazujący podanie pustych pól


- ocena już ocenionego przedmiotu, rys. 17:

 
Rys. 17 – widok informujący o już ocenionym przedmiocie




- informacja o braku zalogowania, po wejściu w koszyk. W takiej sytuacji, przycisk „Kup” jest niedostępny, rys. 18:

 
Rys. 18 – widok obrazujący 
