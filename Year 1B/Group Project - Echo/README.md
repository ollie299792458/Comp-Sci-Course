# EchoGroupProject

We have successfully produced an app that connects to the Informetis API and displays the energy consumption breakdown for a user. The consumption can be viewed overall, by appliance and over time allowing users to really get a feel for where there energy is going. The app compares the user to their past consumption, recommended benchmarks and personal targets to encourage more sustainable usage. This data is analysed and the users are presented with tips in order to help them on this endeavour. Badges can be unlocked which contribute to the unlocking of elements on the users virtual house landscape on the home screen. 

The app is split into 5 screens.
* **Home** - Displays the house landscape, key summary data and “Tip Of The Day”
* **Appliances** - Breaks down energy consumption by appliance
* **Usage** - Reports energy usage over time
* **Badges** - Shows locked and unlocked badges 
* **Tips** - Gives tips for how to reduce consumption specific to the user

### Home Screen

Upon opening the app, users will arrive at the home screen. This is comprised of 3 main parts. The virtual home landscape, consumption summary and “Tip of the day”. 

The virtual home landscape gives users visually appealing feedback of how sustainably they are living and also how well their consumption is improving. The cloud colour, number of trees, flowers, animals and plants, and the mountain all reflect different facets of the users consumption. We wanted to motivate users above and beyond financial incentive and felt that this, whilst giving feedback, would also encourage users to consume more frugally in order to see their landscape improve. 

Ultimately we felt that the people using this app would want to be able to clearly see how much energy they were currently using. The summary displays this in a few different ways. In addition to clearly stating the amount of energy used in absolute terms, there is a bar showing what percentage of their target energy budget for the time period they have consumed. An estimated cost is shown. In order to give a tangible metric that really helps users realise their environmental impact we also display the amount of coal that would have to be burnt to generate that amount of energy. 

Below this we display a tip so that from the homepage, a user can immediately gain relevant insight for how they can improve the environmental  sustainability of their lifestyle though energy usage.

### Appliances Screen

This screen informs users of which appliances are consuming the most energy. A donut chart shows the consumption broken down by category. Below this is a list of the appliances in decreasing order by consumption. 

The graph has a section for each category of appliance. By using the radio buttons above the graph the period of time for which this refers can be changed. Clicking on a section of the graph allows a specific category to be explored in the list below.

The list displays all the appliances. The colour of the appliance corresponds to the colour of the category. The percentage shows what percentage of total energy consumption the device accounts for. Clicking on the appliance in this list presents a graph for usage of that particular appliance.

### Usage screen

On this screen users can view their current and historical usage across different periods of time. 
The graph shows consumption against time and can, again, be set to cover different periods of time. Below that there is a comparison of the usage compared to the previous time period. This gives feedback, gently making users aware if they are consuming more than previously but praising them if usage decreases. Below this the values of plotted on the graph are displayed allowing users to look into this and explore their consumption in more detail. 

### Badges screen

We wanted to provide gamification to motivate users more cleverly than simply telling them how much money they could save. There is a selection of badges available to unlock. Badges change from greyed out to coloured when unlocked. This will also result in a change to the home screen landscape.

The general badges have 4 categories leading to the unlocking of trees, flowers, animals and the mountain. The first 3 of these have 3 different levels representing improved consumption between days, weeks and months. 

Tree badges are earned by users on a daily level. To earn the first tree badge the user has to decrease their daily consumption by 5%, second - by 10%, third - by 15%. 
Flower badges are earned identically but on a weekly basis. Animal badges are also earned on a weekly basis but the percentage levels are different: 10%, 20%, and 30% respectively. The mountain badge is given to the user if their monthly consumption decreases by 10%. 

Specific badges refer to consumption of a specific category of appliance. Appliance specific badges are earned if the weekly consumption on that appliance decreases by 5%.
Once a user has earned a badge, it can be taken away if their energy consumption increases again in the particular area.

Upon clicking on a badge the user has yet to earn, information will pop up explaining how a badge is unlocked. When an earned badge is clicked, the pop up window will explain what this badge corresponds to, e.g. on the main home screen.

### Tips screen
We not only wanted to encourage users to consume less energy but actively help them do so by giving tips specific to them for how they could do so.

There are 2 categories of tips.
Upgrades - This identifies if a specific device is consuming more than benchmarked appliances of that type. This helps establish what appliances are worth upgrading.
Advice - Having looked at usage patterns this offers ways in which a user can change their personal usage.

Tips can be ticked so that they will not constantly be shown even if they are recalculated. 

## System Requiremets

The app requires API version 24 or greater. This is Android version 7 or higher, currently Nougat and Oreo.

## Icons used:
https://www.flaticon.com/packs/space-elements by Freepik
https://www.flaticon.com/free-icon/coal_176598 by Freepik
https://www.flaticon.com/free-icon/cabin_140678 by Smashicons
https://www.flaticon.com/free-icon/sun_146199 by FreePik
https://www.flaticon.com/packs/ecology-5 by FreePik
https://www.flaticon.com/packs/kawaii-animals by FreePik
https://www.flaticon.com/packs/spring-21 by FreePik

FreePik - https://www.flaticon.com/authors/freepik
Smashicons - https://www.flaticon.com/authors/smashicons

## User IDs for testing

The user ID is changed in the UserData file with the line
```java
public static final String CUSTOMER = "0055_UK00000025";
```
where the last 2 digits represent the user ID as shown below

* 4 - three fridges
* 6
* 7
* 8 - v.high usage
* 9
* 12-two TVs
* 15- high outlet load usage but crashes on usage
* 16-very high oven use
* 17 - have a heater!!
* 18
* 19
* 21
* 24
* 25 - heater and PV, good variety, 4 badges
* 26
* 27
* 28 - only two categories
* 29 - outlet load
* 30
* 31 - have a humidifier!
* 32-outlet load
* 33
* 34
* 35 - 4 TVs
* 36
* 37
* 39
* 40
* 41 - use their PV
* 42
* 43
* 44
* 49
* 50
* 53
* 55 - heater usage
* 56
* 57
* 58
* 59
* 62
* 66
* 67

Best to use 25, 29, 41, 31, 55

