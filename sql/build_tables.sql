CREATE TABLE Movies (
    MovieID INT NOT NULL AUTO_INCREMENT,
    MovieTitle VARCHAR(255) NOT NULL DEFAULT '',
    MovieYear INT NOT NULL DEFAULT 0,
    CertificateRating VARCHAR(5) NOT NULL DEFAULT '',
    RentPrice Decimal(5,2) NOT NULL DEFAULT 0.00,
    BuyPrice Decimal(5,2) NOT NULL DEFAULT 0.00,
    MovieValue Decimal(5,2) NOT NULL DEFAULT 0.00,
    Stock INT NOT NULL DEFAULT 0,
    ReleaseDate DATE NOT NULL DEFAULT '2000-01-01',
    OverallReviewRating Decimal(3,1) NOT NULL DEFAULT 0.0,
    Format VARCHAR(45) NULL,
    PRIMARY KEY(MovieID)
);

CREATE TABLE Actors (
    ActorID INT NOT NULL AUTO_INCREMENT,
    FirstName VARCHAR(45) NOT NULL DEFAULT '',
    LastName VARCHAR(45) NOT NULL DEFAULT '',
    PRIMARY KEY (ActorID)
);

CREATE TABLE Movie_Actors (
    MovieID INT NOT NULL DEFAULT 0,
    ActorID INT NOT NULL DEFAULT 0,
    FOREIGN KEY (MovieID) REFERENCES Movies(MovieID)
        ON DELETE CASCADE,
    FOREIGN KEY (ActorID) REFERENCES Actors(ActorID)
        ON DELETE CASCADE
);

CREATE TABLE Directors (
    DirectorID INT NOT NULL AUTO_INCREMENT,
    FirstName VARCHAR(45) NOT NULL DEFAULT '',
    LastName VARCHAR(45) NOT NULL DEFAULT '',
    PRIMARY KEY (DirectorID)
);

CREATE TABLE Movie_Directors (
    MovieID INT NOT NULL DEFAULT 0,
    DirectorID INT NOT NULL DEFAULT 0,
    FOREIGN KEY (MovieID) REFERENCES Movies(MovieID)
        ON DELETE CASCADE,
    FOREIGN KEY (DirectorID) REFERENCES Directors(DirectorID)
        ON DELETE CASCADE
);

CREATE TABLE Genres (
    GenreID INT NOT NULL AUTO_INCREMENT,
    GenreType VARCHAR(45) NOT NULL DEFAULT '',
    PRIMARY KEY (GenreID)
);

CREATE TABLE Movie_Genres (
    MovieID INT NOT NULL DEFAULT 0,
    GenreID INT NOT NULL DEFAULT 0,
    FOREIGN KEY (MovieID) REFERENCES Movies(MovieID)
        ON DELETE CASCADE,
    FOREIGN KEY (GenreID) REFERENCES Genres(GenreID)
        ON DELETE CASCADE
);

CREATE TABLE Employees (
    EmployeeID INT NOT NULL AUTO_INCREMENT,
    FirstName VARCHAR(45) NOT NULL DEFAULT '',
    LastName VARCHAR(45) NOT NULL DEFAULT '',
    Username VARCHAR (45) NOT NULL DEFAULT ' ',
    Password VARCHAR (45) NOT NULL DEFAULT ' ',
    PRIMARY KEY (EmployeeID)
);

CREATE TABLE Customers (
    CustomerID INT NOT NULL AUTO_INCREMENT,
    FirstName VARCHAR(45) NOT NULL DEFAULT '',
    LastName VARCHAR(45) NOT NULL DEFAULT '',
    Username VARCHAR (45) NOT NULL DEFAULT ' ',
    Password VARCHAR (45) NOT NULL DEFAULT ' ',  
    ReferredBy VARCHAR (45),
    CustomerBalance DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    PRIMARY KEY (CustomerID),
    UNIQUE KEY Username_UNIQUE (Username),
    FOREIGN KEY (ReferredBy) REFERENCES Customers(Username)
        ON DELETE CASCADE
);

CREATE TABLE Reviews (
    ReviewID INT NOT NULL AUTO_INCREMENT,
    CustomerID INT NOT NULL DEFAULT 0, 
    MovieID INT NOT NULL DEFAULT 0, 
    Rating INT NOT NULL DEFAULT 0,
    Comment VARCHAR(255),
    PRIMARY KEY (ReviewID),
    FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID)
        ON DELETE CASCADE,
    FOREIGN KEY (MovieID) REFERENCES Movies(MovieID)
        ON DELETE CASCADE
);

CREATE TABLE Transactions (
    TransactionID INT NOT NULL AUTO_INCREMENT,
    CustomerID INT NOT NULL DEFAULT 0,
    MovieID INT NOT NULL DEFAULT 0,
    TransactionDate DATE NOT NULL DEFAULT '2000-01-01',
    UpFrontTransactionCost Decimal(5,2) NOT NULL DEFAULT 0.00,
    isRental BOOLEAN NOT NULL DEFAULT false,
    Watched BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (TransactionID),
    FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID)
        ON DELETE CASCADE,
    FOREIGN KEY (MovieID) REFERENCES Movies(MovieID)
        ON DELETE CASCADE
);

CREATE TABLE Rentals (
    TransactionID INT NOT NULL AUTO_INCREMENT,
    ExpirationDate DATE NOT NULL DEFAULT '2000-01-01',
    ReturnDate DATE,
    LateFee Decimal(5,2),
    LateFeePaid BOOLEAN,
    FOREIGN KEY (TransactionID) REFERENCES Transactions(TransactionID)
        ON DELETE CASCADE
);

CREATE TABLE Configurations (
    NewReleaseRate Decimal (5,2) NOT NULL DEFAULT 4.50,
    NonNewReleaseRate Decimal (5,2) NOT NULL DEFAULT 3.00,
    NewReleasePeriod int NOT NULL DEFAULT 3,
    NonNewReleasePeriod int NOT NULL DEFAULT 4,
    LateFeePerDay Decimal(5,2) NOT NULL DEFAULT 2.00
);

INSERT INTO Configurations SET
    NewReleaseRate = 4.50,
    NonNewReleaseRate = 3.00,
    NewReleasePeriod = 3,
    NonNewReleasePeriod = 4,
    LateFeePerDay = 2.00
);

INSERT INTO Employees SET
    FirstName = "Alex",
    LastName = "Perez",
    Username = "Ronatos",
    Password = "king"
);