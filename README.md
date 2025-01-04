# SimpleWorkShopSoftware
A lightweight, practical tool to simplify your everyday work. Manage customers, their cars, and associated work orders, without the distractions of unnecessary features.

## Basic usage

### 1. First start
- A popup window shows up with a warning message stating that the configuration file doesn't exist yet, you need to create it.
- After confirmation, another window asks you to choose the path where the database will be saved.
- After that, you can choose a path where the program will save the generated PDF of work orders.
- Then the application starts in the main menu, showing two tables, and the table related to car-work order information on the right side.
- There is a chart on the left side showing statistics about the workshop load.
- You can navigate on the left side to go to the settings view and configure your workshop's details.

![First start](https://github.com/attilaeckert/SimpleWorkShopSoftware/blob/main/scrnshots/First%20start.png)

### 2. Settings
- After typing the proper text into the fields, click the button below the text fields to save the configurations.
- You can change the database and work order saving folders anytime here.
- In the upper right text area, type custom text that will appear in the generated PDF's footer section.
- Below, you can choose a logo to appear in the generated PDF's header.

![Settings](https://github.com/attilaeckert/SimpleWorkShopSoftware/blob/main/scrnshots/Settings.png)

### 3. Creating or editing a customer or a car
- On the left side, click the choice box to select the type of the customer you want to save.
- Complete the text fields.
- Click the appropriate button to save, modify, or delete the customer.
- The customer's cars appear in the middle; select one if you want to modify or delete it.
- On the right side, the car creation is basically the same.

![Customer and car](https://github.com/attilaeckert/SimpleWorkShopSoftware/blob/main/scrnshots/Customer%20and%20car%20handling.png)

### 4. Back to main
- Return to the main menu using the upper button on the left.
- In the first table, you see the customer and its car.
- Click on the customer with the needed car to select it.
- Now, it is allowed to set custom service periods for the car, using the left button on the right side.
- Click this button, and a popup will appear, where you can set the custom km/months periods.
- Click the left button to save it, or you can modify it anytime with the right-side button.
- Once it's done, the program will start calculating the remaining months and kilometers based on the actual date and current km.

![Work Orders](https://github.com/attilaeckert/SimpleWorkShopSoftware/blob/main/scrnshots/Main%20menu.png) 
![Service periods](https://github.com/attilaeckert/SimpleWorkShopSoftware/blob/main/scrnshots/Service%20periods%20popup.png)

### 5. Work orders
- To add a work order to the selected car, click the choice box and select the type.
- First, a request needs to be created with the customer's issue regarding the car.
- Fill the text fields with the proper text, then add some labor or parts to the table to calculate with them.
- You can set the discount percentage for the parts; by default, it calculates with 0%.
- Click the button below to save.

![Create Request](https://github.com/attilaeckert/SimpleWorkShopSoftware/blob/main/scrnshots/Create%20Request.png)

- If a request is created, you can step back to the main menu and select it from the second table.
- Once the request is selected, it is enabled to create a work order from this view.
- The work order creation process is very similar to the request creation.
- You can add the works you've done and parts used on the car.
- In the middle, a button appears above the table, and when you click it, a popup window will show up.
- In this window, you can set the services you've performed to reset on the car.
- After that, you can save this too.

![Update periods](https://github.com/attilaeckert/SimpleWorkShopSoftware/blob/main/scrnshots/Update%20periods%20popup.png) 
![Create Work order](https://github.com/attilaeckert/SimpleWorkShopSoftware/blob/main/scrnshots/Create%20Work%20order.png)

### 6. PDF creation
- Return to the main menu, and when you select any type of work order, you can create a PDF from it using the upper button on the left.
- On the text area above the buttons, a short version of the work order will appear.

![Create PDF](https://github.com/attilaeckert/SimpleWorkShopSoftware/blob/main/scrnshots/Sample%20Pdf.png)

- You can create a document for internal use in the workshop to document the vehicle's condition by selecting a request and clicking the lower button.

![Create internal PDF](https://github.com/attilaeckert/SimpleWorkShopSoftware/blob/main/scrnshots/Condition%20Pdf.png)