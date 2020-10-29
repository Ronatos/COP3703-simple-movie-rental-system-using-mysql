1. Login
    a. User enters 'Username' and 'Password', which will be inserted in to a query of the Customers table and the Employees table. If an entry with any combination of matching values exists, print the appropriate message.
        - No matching 'Username' -> "Incorrect username"
        - Matching 'Username' & non-matching 'Password' -> "Incorrect password"
        - Matching 'Username' & matching 'Password' -> Login successful
    b. User is determined to be an Employee or a Customer based on which table contained the matching 'Username' & 'Password'.
        - If the user is an Employee, they are presented with the Employee dashboard.
        - If the user is an Customer, they are presented with the Customer dashboard.
2. Dashboard is presented.
    a. Employee dashboard
        I. Locate movie
            1. Ask for movie attribute to search by, and provide relevant list
        II. Update inventory
            1. Add new item
                a. Add Movie (fillable form)
                    - Title
                    - Release Date
                    - Certificate Rating
                    - Business cost per item
                    - Customer purchase cost
                    - Customer rental cost
		    - Back
                b. Add Actor (fillable form)
                    - First name
                    - Last name
                c. Add Genre (fillable form)
                    - Genre name
                d. Add Director (fillable form)
                    - First name
                    - Last name
            2. Update existing item
                a. Update Movie
                    I. Search for movie
                    II. Determine which value to update
                        - Title
                        - Release Date
                        - Certificate Rating
                        - Business cost per item
                        - Customer purchase cost
                        - Customer rental cost
                        - Stock
                b. Update Actor
                    I. Search for actor
                    II. Determine which value to update
                        - First name
                        - Last name
                c. Update Genre
                    I. Search for Genre
                        - Genre name
                d. Update Director
                    I. Search for Director
                    II. Determine which value to update
                        - First name
                        - Last name
            3. Link existing items
            	a. Add actor to movie
            	b. Add director to movie
            	c. Add genre to movie
            4. Delete an item
                a. Delete Movie
                    I. Search for movie
                    II. Ask if the employee is sure they want to delete this item
                b. Delete Actor
                    I. Search for actor
                    II. Ask if the employee is sure they want to delete this item
                c. Delete Genre
                    I. Search for genre
                    II. Ask if the employee is sure they want to delete this item
                d. Delete Director
                    I. Search for director
                    II. Ask if the employee is sure they want to delete this item
        III. Customer management
            1. Check customer balance
                a. Search for Customer
                    - Search by CustomerID
                    - Search by FirstName & LastName (this will pull all customers with the same first and last name)
                b. Database is queried to retrieve 'AccountBalance' & unpaid 'LateFee's
            2. Apply late fee
                a. Search for Customer
                    - Search by CustomerID
                    - Search by FirstName & LastName (this will pull all customers with the same first and last name)
                b. Database is queried to retrieve rentals with 'LateFeePaid' = FALSE, and all rented movies with relevant inf is displayed.
                c. Select rental to apply late fee to by TransactionID
                    - Enter late fee value to apply
        IV. Reports
            1. Revenue report from sales and rentals
		a. Run report by movie title
		b. Run report by movie genre
	    2. Revenue report from sales and rentals over time
		a. Start date
		b. End date
	    3. Back to Employee Dashboard
	V. Log out
    b. Customer dashboard
        I. Find a movie
            1. Provide recommended movies
            2. Ask for movie attribute to search by, and provide relevant list
                - 'Buy', 'Rent', and 'Write Review' should be listed options under each movie (somehow? depends on whether this is a terminal or web app. pagination? Write review should only show if you've purchased or rented this movie before)
	    3. Back to dashboard
        II. Rental return
            1. Query database to display list of ongoing rentals
                - Late fees applied here if needed
            2. Select rental by TransactionID
            3. Ask if the customer is sure they'd like to return this movie
                - If yes, add 'LateFee' to 'AccountBalance'
                - If no, kick the customer out of the rental return menu
        III. Account management
            1. Personal Information
                a. Update First Name
                b. Update Last Name
            2. Manage Account Balance
                a. Add funds
                    - Dummy card information system. Outside the scope of the class.
            3. Delete Account