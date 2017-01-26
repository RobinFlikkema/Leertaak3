class Station {

    constructor(id, longitude, latitude, measurements){
        this.id_number = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.measurements = measurements;
    }

    addMeasurement(measurement){
        measurements.push(measurement);
    }

    printDetails(){
        console.log("Printing data from station " + this.id_number);
        console.log("-----------------------------------");
        console.log(this.longitude + " : " + this.latitude);
        console.log("-----------------------------------");
        $.each(this.measurements, function (measurement_number, data) {
            console.log("MEASUREMENT " + measurement_number);
            $.each(data, function (value_type, value) {
                console.log(value_type + " : " + value);
            });
        });
        console.log("");
    }
}