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
![Adiabatická rovnice tlaku](http://latex.codecogs.com/svg.latex?\\frac{p_{4}}{p_{5}}=r^{\\gamma})
![Adiabatická rovnice teploty](http://latex.codecogs.com/svg.latex?\\frac{T_{4}}{T_{5}}=r^{\\gamma-1})
#### Rovnice pro výpočet práce
![Rovnice pro výpočet práce](http://latex.codecogs.com/svg.latex?W=c_{v}\\times\\rho_{vzduch}\\times V_{2}\\times[(T_{4}-T_{3})-(T_{5}-T_{2})])
#### Rovnice pro výpočet výkonu
![Rovnice pro výpočet práce](http://latex.codecogs.com/svg.latex?P=\\frac{rpm}{2\\times60}\\times W)
## GUI
![GUI](https://raw.githubusercontent.com/LuSo58/heat_machine_simulation/master/preview.png)



<div>
<svg height='29.6637pt' version='1.1' viewBox='0 0 38.5318 29.6637' width='38.5318pt' xmlns='http://www.w3.org/2000/svg' xmlns:xlink='http://www.w3.org/1999/xlink'>
<defs>
<path d='M7.428 -6.864C7.836 -7.512 8.208 -7.8 8.82 -7.848C8.94 -7.86 9.036 -7.86 9.036 -8.076C9.036 -8.124 9.012 -8.196 8.904 -8.196C8.688 -8.196 8.172 -8.172 7.956 -8.172C7.608 -8.172 7.248 -8.196 6.912 -8.196C6.816 -8.196 6.696 -8.196 6.696 -7.968C6.696 -7.86 6.804 -7.848 6.852 -7.848C7.296 -7.812 7.344 -7.596 7.344 -7.452C7.344 -7.272 7.176 -6.996 7.164 -6.984L3.396 -1.008L2.556 -7.476C2.556 -7.824 3.18 -7.848 3.312 -7.848C3.492 -7.848 3.6 -7.848 3.6 -8.076C3.6 -8.196 3.468 -8.196 3.432 -8.196C3.228 -8.196 2.988 -8.172 2.784 -8.172H2.112C1.236 -8.172 0.876 -8.196 0.864 -8.196C0.792 -8.196 0.648 -8.196 0.648 -7.98C0.648 -7.848 0.732 -7.848 0.924 -7.848C1.536 -7.848 1.572 -7.74 1.608 -7.44L2.568 -0.036C2.604 0.216 2.604 0.252 2.772 0.252C2.916 0.252 2.976 0.216 3.096 0.024L7.428 -6.864Z' id='g0-86'/>
<path d='M4.668 -4.908C4.296 -4.836 4.104 -4.572 4.104 -4.308C4.104 -4.02 4.332 -3.924 4.5 -3.924C4.836 -3.924 5.112 -4.212 5.112 -4.572C5.112 -4.956 4.74 -5.292 4.14 -5.292C3.66 -5.292 3.108 -5.076 2.604 -4.344C2.52 -4.98 2.04 -5.292 1.56 -5.292C1.092 -5.292 0.852 -4.932 0.708 -4.668C0.504 -4.236 0.324 -3.516 0.324 -3.456C0.324 -3.408 0.372 -3.348 0.456 -3.348C0.552 -3.348 0.564 -3.36 0.636 -3.636C0.816 -4.356 1.044 -5.052 1.524 -5.052C1.812 -5.052 1.896 -4.848 1.896 -4.5C1.896 -4.236 1.776 -3.768 1.692 -3.396L1.356 -2.1C1.308 -1.872 1.176 -1.332 1.116 -1.116C1.032 -0.804 0.9 -0.24 0.9 -0.18C0.9 -0.012 1.032 0.12 1.212 0.12C1.344 0.12 1.572 0.036 1.644 -0.204C1.68 -0.3 2.124 -2.112 2.196 -2.388C2.256 -2.652 2.328 -2.904 2.388 -3.168C2.436 -3.336 2.484 -3.528 2.52 -3.684C2.556 -3.792 2.88 -4.38 3.18 -4.644C3.324 -4.776 3.636 -5.052 4.128 -5.052C4.32 -5.052 4.512 -5.016 4.668 -4.908Z' id='g0-114'/>
<path d='M8.1 -3.888C8.268 -3.888 8.484 -3.888 8.484 -4.104C8.484 -4.332 8.28 -4.332 8.1 -4.332H1.032C0.864 -4.332 0.648 -4.332 0.648 -4.116C0.648 -3.888 0.852 -3.888 1.032 -3.888H8.1ZM8.1 -1.656C8.268 -1.656 8.484 -1.656 8.484 -1.872C8.484 -2.1 8.28 -2.1 8.1 -2.1H1.032C0.864 -2.1 0.648 -2.1 0.648 -1.884C0.648 -1.656 0.852 -1.656 1.032 -1.656H8.1Z' id='g1-61'/>
<path d='M2.256 -1.632C2.384 -1.752 2.72 -2.016 2.848 -2.128C3.344 -2.584 3.816 -3.024 3.816 -3.752C3.816 -4.704 3.016 -5.32 2.016 -5.32C1.056 -5.32 0.424 -4.592 0.424 -3.88C0.424 -3.488 0.736 -3.432 0.848 -3.432C1.016 -3.432 1.264 -3.552 1.264 -3.856C1.264 -4.272 0.864 -4.272 0.768 -4.272C1 -4.856 1.536 -5.056 1.928 -5.056C2.672 -5.056 3.056 -4.424 3.056 -3.752C3.056 -2.92 2.472 -2.312 1.528 -1.344L0.52 -0.304C0.424 -0.216 0.424 -0.2 0.424 0H3.584L3.816 -1.432H3.568C3.544 -1.272 3.48 -0.872 3.384 -0.72C3.336 -0.656 2.728 -0.656 2.6 -0.656H1.176L2.256 -1.632Z' id='g2-50'/>
<path d='M2.024 -2.672C2.656 -2.672 3.056 -2.208 3.056 -1.368C3.056 -0.368 2.488 -0.072 2.064 -0.072C1.624 -0.072 1.024 -0.232 0.744 -0.656C1.032 -0.656 1.232 -0.84 1.232 -1.104C1.232 -1.36 1.048 -1.544 0.792 -1.544C0.576 -1.544 0.352 -1.408 0.352 -1.088C0.352 -0.328 1.168 0.168 2.08 0.168C3.144 0.168 3.888 -0.568 3.888 -1.368C3.888 -2.032 3.36 -2.64 2.544 -2.816C3.176 -3.04 3.648 -3.584 3.648 -4.224S2.928 -5.32 2.096 -5.32C1.24 -5.32 0.592 -4.856 0.592 -4.248C0.592 -3.952 0.792 -3.824 1 -3.824C1.248 -3.824 1.408 -4 1.408 -4.232C1.408 -4.528 1.152 -4.64 0.976 -4.648C1.312 -5.088 1.928 -5.112 2.072 -5.112C2.28 -5.112 2.888 -5.048 2.888 -4.224C2.888 -3.664 2.656 -3.328 2.544 -3.2C2.304 -2.952 2.12 -2.936 1.632 -2.904C1.48 -2.896 1.416 -2.888 1.416 -2.784C1.416 -2.672 1.488 -2.672 1.624 -2.672H2.024Z' id='g2-51'/>
</defs>
<g id='page1' transform='matrix(1.12578 0 0 1.12578 -63.986 -61.02)'>
<use x='56.6248' xlink:href='#g0-114' y='70.3181'/>
<use x='65.5796' xlink:href='#g1-61' y='70.3181'/>
<use x='79.2516' xlink:href='#g0-86' y='62.2'/>
<use x='86.1016' xlink:href='#g2-50' y='64'/>
<rect height='0.47998' width='11.6' x='79.2516' y='67.0781'/>
<use x='79.2516' xlink:href='#g0-86' y='78.5495'/>
<use x='86.1016' xlink:href='#g2-51' y='80.3495'/>
</g>
</svg>
</div>
