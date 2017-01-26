class Station {

    constructor(id, longitude, latitude){
        this.id_number = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.measurements = [];
    }

    addMeasurement(measurement){
        measurements.push(measurement);
    }

    printDetails(){
        console.log(id_number + " : " + longitude + " : " + latitude);
    }
}