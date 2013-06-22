fsupb
=====


Diese App wird entwickelt, um möglichst schnell an wichtige Informationen über die Informatik-Fachschaft Paderborn zu kommen. Aktuell per App abrufbar:
* Öffnungsstatus des Fachschaftsbüros
* Termin der nächsten Fachschaftsratssitzung

Zudem gibt es ein Widget, das den Öffnungsstatus direkt auf dem Homescreen anzeigt.


=====


Abhängigkeiten: 
* https://github.com/ManuelPeinado/RefreshActionItem
* http://actionbarsherlock.com/
* https://github.com/keyboardsurfer/Crouton
* libraries ($project/library) als Android library project in bspw eclipse importieren
* fsupb -> RefreshActionItem -> Crouton -> actionbarsherlock (Abhängigkeiten)
 
Es wird zur Zeit kein Maven o.ä. verwendet, da dadurch die Build-Zeit sich ca. verdrei- oder vervierfachen würde in meinem Fall.



