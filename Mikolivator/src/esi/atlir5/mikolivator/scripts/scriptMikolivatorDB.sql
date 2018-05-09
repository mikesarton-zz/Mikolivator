CREATE TABLE Simulation (
    SimID int NOT NULL,
    DateDebut Timestamp NOT NULL,
    DateFin Timestamp NOT NULL,
    nbPrsGenerated int NOT NULL,
    nbPrsElevator int NOT NULL
);

CREATE TABLE Floor (
    FloorID int NOT NULL,
    numFloor int NOT NULL,
    nbPrsTotal int NOT NULL,
    Simulation int NOT NULL
);

CREATE TABLE Evacuation (
  EvacID int NOT NULL,
  FloorID int NOT NULL,
  nbPeopleEvacuated int NOT NULL
);

CREATE TABLE Sequence (
  Simulation int NOT NULL,
  Floor int NOT NULL,
  Evacuation int NOT NULL
);

ALTER TABLE Simulation ADD CONSTRAINT pkSimID PRIMARY KEY(SimID);
ALTER TABLE Floor ADD CONSTRAINT pkFloorID PRIMARY KEY(FloorID);
ALTER TABLE Evacuation ADD CONSTRAINT pkEvacID PRIMARY KEY(EvacID);

ALTER TABLE Floor ADD CONSTRAINT fkSimulation FOREIGN KEY(Simulation) REFERENCES Simulation(SimID);
ALTER TABLE Evacuation ADD CONSTRAINT fkFloorID FOREIGN KEY(FloorID) REFERENCES Floor(FloorID);

INSERT INTO Sequence (Simulation, Floor, Evacuation) VALUES (0,0,0);
