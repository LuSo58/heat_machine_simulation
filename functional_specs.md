# Funkční specifikace

## O tomto dokumentu
Tento dokument obsahuje teoretický návrh programu pro vývojáře tohoto projektu a pro učitele SSPŠ pro hodnocení tohoto projektu.

## Start programu
 * Vytvořit GUI, připravit renderovací pole
 * Vyplnit výchozí hodnoty
 * Vyčkat na vstup uživatele (dynamicky vypočítávat závislé proměnné)
    * Uživatel zadá hodnoty
    * Spustí simulaci
    * Kontrola uživatelových vstupů
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
    * Vymazat závislé
    * Uvolnit vstupní pole
    
   
## Vstupní hodnoty
Značka|popis|základní hodnota
---|---|---
V<sub>2</sub>|Objem válce|závisí na motoru
V<sub>3</sub>|Objem při kompresi|závisí na motoru
T<sub>2</sub>|Venkovní teplota|16°C
p<sub>2</sub>|Atmosférický tlak|0.1MPa
Q|Výhřevnost paliva|diesel = 43400 kJ&times;kg<sup>-1</sup>
f|Poměr vzduch/palivo|diesel = 0.02
rpm|Otáčky za minutu|2000 rpm
n<sub>válce</sub>|Počet válců|závisí na motoru
&eta;|Účinnost|0.4
 
## Konstanty a závislé proměnné
Značka|popis|hodnota
---|---|---
r|kompresní poměr|závisí na V<sub>2</sub> a V<sub>3</sub>
&rho;<sub>vzduch</sub>|hustota vzduchu|1.29 kg&times;m<sup>3</sup>
c<sub>V</sub>|Měrná tepelná kapacita vzduchu|0.72 kJ&times;kg<sup>-1</sup>&times;K<sup>-1</sup>
&gamma;|Poissonova konstanta|vzduch = 1,4    
## Rovnice
#### Rovnice pro výpočet kompresního poměru
![Rovnice pro výpočet práce](http://latex.codecogs.com/svg.latex?r=\\frac{V_{2}}{V_{3}})
#### Adiabatické rovnice
![Adiabatická rovnice tlaku](http://latex.codecogs.com/svg.latex?\\frac{p_{3}}{p_{2}}=r^{\\gamma})
![Adiabatická rovnice teploty](http://latex.codecogs.com/svg.latex?\\frac{T_{3}}{T_{2}}=r^{\\gamma-1})
#### Výhřevnost paliva
![Výhřevnost paliva](http://latex.codecogs.com/svg.latex?T_{4}=T_{3}+\\frac{Q}{f\\times c_{v}})
#### Stavová isochorická rovnice
![Stavová isochorická rovnice](http://latex.codecogs.com/svg.latex?\\frac{p_{4}}{p_{3}}=\\frac{T_{4}}{T_{3}})
#### Adiabatické rovnice
![Adiabatická rovnice tlaku](http://latex.codecogs.com/svg.latex?\\frac{p_{5}}{p_{4}}=r^{-\\gamma})
![Adiabatická rovnice teploty](http://latex.codecogs.com/svg.latex?\\frac{T_{5}}{T_{4}}=r^{1-\\gamma})
#### Rovnice pro výpočet práce
![Rovnice pro výpočet práce](http://latex.codecogs.com/svg.latex?W=c_{v}\\times\\rho_{vzduch}\\times V_{2}\\times[(T_{4}-T_{3})-(T_{5}-T_{2})]\\times\\eta)
#### Rovnice pro výpočet výkonu
![Rovnice pro výpočet práce](http://latex.codecogs.com/svg.latex?P=\\frac{rpm}{2\\times60}\\times W\\times n)
## GUI
![GUI](https://raw.githubusercontent.com/LuSo58/heat_machine_simulation/master/preview.png)
