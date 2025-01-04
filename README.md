# SimpleWorkShopSoftware
This JavaFX desktop application is designed for professionals who need a simple, efficient tool to manage workshop-related tasks, focusing on essential operations like customer and car management, as well as work order creation.


## Basic usage

### 1. First start
- A popup window shows up with a warning message telling that the configurations doesn't exists yet, so you need to create it
- After the confirmation another window asks to choose the path where the database saved
- After that you can choose a path where the program saves the generated Pdf of work orders
- Then the application starts in the main menu, showing two tables, and the table related car-work order informations on the right side
- There is a chart on the left side showing statistics about the load of the workshop.
- You can navigate on the left side to get to the settings view, and configurate your workshop's details

![First start] (https://github.com/attilaeckert/SimpleWorkShopSoftware/blob/main/scrnshots/First%20start.png)

### 2. Settings
- After typing the proper text to the fields, click on the button below the textfields, to save the configurations
- You can change the Database and work order saving folders any time here
- On the upper right text area, type a custom text, that will appear in the generated pdf tail section
- Below you can choose a logo, to be appear in the generated pdf header

![Settings](https://github.com/attilaeckert/SimpleWorkShopSoftware/blob/main/scrnshots/Settings.png)

### 3. Creating or editing a customer or a car
- On the left side click on the choose box, to select the type of the customer you want to save
- Complete the text fields
- Click on the proper button to save, modify, or delete the customer.
- The customer's cars appear in the middle, select one, if you want to modify or delete.
- On the right side the car creating is basically the same.

![Customer and car](https://github.com/attilaeckert/SimpleWorkShopSoftware/blob/main/scrnshots/Customer%20and%20car%20handling.png)

### 4. Back to main
- Get back to main menu using the upper button on the left
- In the first table you see the customer, and its car.
- Click on the customer, with the needed car to select it.
- Now it is allowed to set custom service periods for the car, with the left button on the right side
- Click on this button, then a popup will show, where you set the custom km/months periods.
- Click on the left button to save it, or you can modify it whenever you want with the right side button
- Once it's done, the program starts to calculate with the actual date and actual km, to display the remaining months and kilometers.

![Work Orders](https://github.com/attilaeckert/SimpleWorkShopSoftware/blob/main/scrnshots/Main%20menu.png)
![Service periods] (https://github.com/attilaeckert/SimpleWorkShopSoftware/blob/main/scrnshots/Service%20periods%20popup.png)

### 5. Work orders
- Now to add a work order to the selected car
- Click on the choice box, and select type
- First a request need to be created, with the customer's issue with the car
- Fill the text fields with the proper text, then add some labor, or parts to the table, to calculate with it
- You can set the discount percentage for the parts, by default it calculates with 0%
- Click on the button below to save


![Create Request](https://github.com/attilaeckert/SimpleWorkShopSoftware/blob/main/scrnshots/Create%20Request.png)

- If a request created, you can step back to the main menu, and select it from the second table.
- Once the request is selected, it is enabled to create a work order from this view.
- The work order creation is very similar to the request creation
- You can add the works you've done and parts used on the car.
- In the middle a button appears above the table, and when you clicked it, a popup window shows up.
- Within this window, you can set the services you've done to reset on the car.
- After it, you can save this too.

![Update periods] (https://github.com/attilaeckert/SimpleWorkShopSoftware/blob/main/scrnshots/Update%20periods%20popup.png)
![Create Work order] (https://github.com/attilaeckert/SimpleWorkShopSoftware/blob/main/scrnshots/Create%20Work%20order.png)

### 6. Pdf creation

- Go back to main, and when you select any type of work order, you can create a pdf from it, with the upper button on the right left.
- On the text area above the buttons a short version of the work order appear.

![Create Pdf] (https://github.com/attilaeckert/SimpleWorkShopSoftware/blob/main/scrnshots/Sample%20Pdf.png)

- You can create a document for internal use in the workshop to document the vehicles condition, when selecting a request, and clicking on the lower button.
