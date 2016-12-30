# Funkční specifikace

## Start programu
 * Vytvořit GUI, připravit rederovací pole
 * Vyplnit výchozí hodnoty
 * Vyčkat na vstup uživatele (dynamicky vypočítávat závyslé proměnné)
    * Uživatel zadá hodnoty
    * Spustí simulaci
    * Kontrola uživatelovích vstupů
        1. OK &rarr; pokračovat
        2. Špatné &rarr; upozornit na špatná pole &rarr; vyčkat na vstup uživatele
    * Zamrazit vstupní pole
    * Vypočítat výkon
    * Zobrazit výkon
    * Vykreslit vzorový PV diagram
    * Spustit animaci PV diagramu
    * Spustit animaci motoru
 * Vyčkat na příkaz ukončení animace
    * Ukončit animace
    * Vymazat závyslé
    * Uvolnit vstupní pole

## Vstupní hodnoty
Značka|popis|základní hodnota
---|---|---
V<sub>1</sub>|Objem válce|závisí na motoru
V<sub>2</sub>|Objem při kompresi|závisí na motoru
T<sub>1</sub>|Venkovní teplota|16°C
p<sub>1</sub>|Atmosférický tlak|0.1MPa
Q|Výhřevnost paliva|diesel = 43400 kJ&times;kg<sup>-1</sup>
f|Poměr palivo/vzduch|diesel = 0.035
rpm|Otáčky|rpm
&eta;|Účinnost|0.4
 
## Konstanty a závyslé proměnné
Značka|popis|hodnota
---|---|---
r|kompresní poměr|závisí na V<sub>2</sub> a V<sub>3</sub>
&rho;<sub>vzduch</sub>|hustota vzduchu|1.29 kg&times;m<sup>3</sup>
&gamma;|Poissonova konstana|vzduch = 1,4
