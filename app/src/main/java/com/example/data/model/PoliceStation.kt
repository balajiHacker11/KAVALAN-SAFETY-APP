package com.example.data.model

data class PoliceStation(
    val id: String,
    val name: String,
    val district: String,
    val address: String,
    val phone: String,
    val pincode: String,
    val latitude: Double,
    val longitude: Double,
    val jurisdiction: String
)

object PoliceStationProvider {
    val tnPoliceStations = listOf(
        // CHENNAI CITY
        PoliceStation(
            id = "awps_chennai_01",
            name = "AWPS Kilpauk (W-1)",
            district = "Chennai",
            address = "Ormes Road, Kilpauk, Chennai, Tamil Nadu",
            phone = "044-23452331",
            pincode = "600010",
            latitude = 13.0802,
            longitude = 80.2412,
            jurisdiction = "Kilpauk, Chetpet, Purasawalkam"
        ),
        PoliceStation(
            id = "awps_chennai_02",
            name = "AWPS Thousand Lights (W-2)",
            district = "Chennai",
            address = "Greams Road, Thousand Lights, Chennai, Tamil Nadu",
            phone = "044-23452332",
            pincode = "600006",
            latitude = 13.0601,
            longitude = 80.2520,
            jurisdiction = "Greams Road, Nungambakkam, Gopalapuram"
        ),
        PoliceStation(
            id = "awps_chennai_03",
            name = "AWPS Egmore (W-3)",
            district = "Chennai",
            address = "Pantheon Road, Egmore, Chennai, Tamil Nadu",
            phone = "044-23452333",
            pincode = "600008",
            latitude = 13.0732,
            longitude = 80.2611,
            jurisdiction = "Egmore Railway Area, Chintadripet"
        ),
        PoliceStation(
            id = "awps_chennai_04",
            name = "AWPS Adyar (W-8)",
            district = "Chennai",
            address = "Lattice Bridge Road, Adyar, Chennai, Tamil Nadu",
            phone = "044-23452338",
            pincode = "600020",
            latitude = 13.0012,
            longitude = 80.2565,
            jurisdiction = "Adyar, Besant Nagar, Thiruvanmiyur"
        ),
        PoliceStation(
            id = "awps_chennai_05",
            name = "AWPS Guindy (W-9)",
            district = "Chennai",
            address = "GST Road, Guindy, Chennai, Tamil Nadu",
            phone = "044-23452339",
            pincode = "600032",
            latitude = 13.0067,
            longitude = 80.2021,
            jurisdiction = "Guindy Industrial Estate, Saidapet, Ekkatuthangal"
        ),
        PoliceStation(
            id = "awps_chennai_06",
            name = "AWPS Ambattur (W-12)",
            district = "Chennai",
            address = "MTH Road, Ambattur OT, Chennai, Tamil Nadu",
            phone = "044-23452342",
            pincode = "600053",
            latitude = 13.1143,
            longitude = 80.1548,
            jurisdiction = "Ambattur OT, Industrial Estate, Mogappair"
        ),
        PoliceStation(
            id = "awps_chennai_07",
            name = "AWPS Tambaram",
            district = "Chengalpattu / Tambaram",
            address = "GST Road, West Tambaram, Chennai, Tamil Nadu",
            phone = "044-22264445",
            pincode = "600045",
            latitude = 12.9249,
            longitude = 80.1168,
            jurisdiction = "Tambaram, Chromepet, Sanatorium"
        ),

        // MADURAI
        PoliceStation(
            id = "awps_madurai_01",
            name = "AWPS Madurai South",
            district = "Madurai",
            address = "Periyar Bus Stand Area, Madurai Town, Tamil Nadu",
            phone = "0452-2345001",
            pincode = "625001",
            latitude = 9.9195,
            longitude = 78.1193,
            jurisdiction = "Madurai Town, Crime Branch South, Mahal Area"
        ),
        PoliceStation(
            id = "awps_madurai_02",
            name = "AWPS Madurai North (Tallakulam)",
            district = "Madurai",
            address = "Tallakulam Main Road, Madurai, Tamil Nadu",
            phone = "0452-2345002",
            pincode = "625002",
            latitude = 9.9324,
            longitude = 78.1345,
            jurisdiction = "Tallakulam, KK Nagar, Anna Nagar"
        ),
        PoliceStation(
            id = "awps_madurai_03",
            name = "AWPS Thiruparankundram",
            district = "Madurai",
            address = "GST Road, Thiruparankundram, Madurai, Tamil Nadu",
            phone = "0452-2345003",
            pincode = "625005",
            latitude = 9.8812,
            longitude = 78.0711,
            jurisdiction = "Thiruparankundram, Pasumalai, Austinpatti"
        ),

        // COIMBATORE
        PoliceStation(
            id = "awps_cbe_01",
            name = "AWPS Coimbatore Central",
            district = "Coimbatore",
            address = "Goods Shed Road, Near Railway Station, Coimbatore, Tamil Nadu",
            phone = "0422-2300055",
            pincode = "641018",
            latitude = 11.0018,
            longitude = 76.9629,
            jurisdiction = "Ukadam, Town Hall, Gandhipuram"
        ),
        PoliceStation(
            id = "awps_cbe_02",
            name = "AWPS Coimbatore East (Singanallur)",
            district = "Coimbatore",
            address = "Trichy Road, Singanallur, Coimbatore, Tamil Nadu",
            phone = "0422-2300056",
            pincode = "641005",
            latitude = 11.0010,
            longitude = 77.0255,
            jurisdiction = "Singanallur, Peelamedu, Hope College, TIDEL Park"
        ),
        PoliceStation(
            id = "awps_cbe_03",
            name = "AWPS Pollachi",
            district = "Coimbatore",
            address = "New Scheme Road, Pollachi, Tamil Nadu",
            phone = "04259-223344",
            pincode = "642001",
            latitude = 10.6581,
            longitude = 77.0083,
            jurisdiction = "Pollachi Town, Mahalingapuram, Anaimalai"
        ),

        // TIRUCHIRAPPALLI (TRICHY)
        PoliceStation(
            id = "awps_trichy_01",
            name = "AWPS Trichy Fort",
            district = "Tiruchirappalli",
            address = "Near Main Guard Gate, Fort Area, Trichy, Tamil Nadu",
            phone = "0431-2704100",
            pincode = "620002",
            latitude = 10.8282,
            longitude = 78.6942,
            jurisdiction = "Fort Station, Chatram Bus Stand, Rockfort"
        ),
        PoliceStation(
            id = "awps_trichy_02",
            name = "AWPS Trichy Cantonment",
            district = "Tiruchirappalli",
            address = "Collector Office Road, Cantonment, Trichy, Tamil Nadu",
            phone = "0431-2704101",
            pincode = "620001",
            latitude = 10.8041,
            longitude = 78.6823,
            jurisdiction = "Cantonment, Central Bus Stand, KK Nagar"
        ),

        // SALEM
        PoliceStation(
            id = "awps_salem_01",
            name = "AWPS Salem Town",
            district = "Salem",
            address = "Bretts Road, Near Old Bus Stand, Salem, Tamil Nadu",
            phone = "0427-2210100",
            pincode = "636001",
            latitude = 11.6539,
            longitude = 78.1583,
            jurisdiction = "Salem Town, Fort, Shevapet"
        ),
        PoliceStation(
            id = "awps_salem_02",
            name = "AWPS Suramangalam (Salem West)",
            district = "Salem",
            address = "Junction Main Road, Suramangalam, Salem, Tamil Nadu",
            phone = "0427-2210101",
            pincode = "636005",
            latitude = 11.6781,
            longitude = 78.1189,
            jurisdiction = "Salem Junction Railway Station, Suramangalam"
        ),

        // TIRUNELVELI
        PoliceStation(
            id = "awps_tvl_01",
            name = "AWPS Tirunelveli Town",
            district = "Tirunelveli",
            address = "Swamy Nellaiappar High Road, Tirunelveli Town, Tamil Nadu",
            phone = "0462-2330100",
            pincode = "627006",
            latitude = 8.7282,
            longitude = 77.6891,
            jurisdiction = "Nellaiappar Temple Area, Pettai"
        ),
        PoliceStation(
            id = "awps_tvl_02",
            name = "AWPS Palayamkottai",
            district = "Tirunelveli",
            address = "High Ground Road, Palayamkottai, Tirunelveli, Tamil Nadu",
            phone = "0462-2330101",
            pincode = "627002",
            latitude = 8.7132,
            longitude = 77.7311,
            jurisdiction = "Palayamkottai, Medical College Hospital, NGO Colony"
        ),

        // THANJAVUR
        PoliceStation(
            id = "awps_tj_01",
            name = "AWPS Thanjavur Town",
            district = "Thanjavur",
            address = "South Rampart, Near Big Temple, Thanjavur, Tamil Nadu",
            phone = "04362-230100",
            pincode = "613001",
            latitude = 10.7870,
            longitude = 79.1378,
            jurisdiction = "Thanjavur Old Bus Stand, Big Temple, Palace"
        ),
        PoliceStation(
            id = "awps_tj_02",
            name = "AWPS Kumbakonam",
            district = "Thanjavur",
            address = "Dr. Besant Road, Kumbakonam, Tamil Nadu",
            phone = "0435-2430100",
            pincode = "612001",
            latitude = 10.9617,
            longitude = 79.3881,
            jurisdiction = "Kumbakonam Town, Mahamaham Tank, Railway Area"
        ),

        // VELLORE
        PoliceStation(
            id = "awps_vellore_01",
            name = "AWPS Vellore Town",
            district = "Vellore",
            address = "Infantry Road, Near Fort, Vellore, Tamil Nadu",
            phone = "0416-2220100",
            pincode = "632001",
            latitude = 12.9165,
            longitude = 79.1325,
            jurisdiction = "Vellore Fort, CMC Hospital Area, Katpadi Junction"
        ),

        // ERODE
        PoliceStation(
            id = "awps_erode_01",
            name = "AWPS Erode Town",
            district = "Erode",
            address = "Brough Road, Erode, Tamil Nadu",
            phone = "0424-2250100",
            pincode = "638001",
            latitude = 11.3410,
            longitude = 77.7172,
            jurisdiction = "Erode Bus Stand, Railway Colony, Perundurai Road"
        ),

        // TIRUPPUR
        PoliceStation(
            id = "awps_tiruppur_01",
            name = "AWPS Tiruppur North",
            district = "Tiruppur",
            address = "Kumaran Road, Tiruppur, Tamil Nadu",
            phone = "0421-2200100",
            pincode = "641601",
            latitude = 11.1085,
            longitude = 77.3411,
            jurisdiction = "Tiruppur Railway Station, New Bus Stand, Apparel Park"
        ),

        // KANCHIPURAM / CUDDALORE / DINDIGUL / THENI / KANYAKUMARI
        PoliceStation(
            id = "awps_kanchi_01",
            name = "AWPS Kanchipuram Town",
            district = "Kanchipuram",
            address = "West Raja Street, Kanchipuram, Tamil Nadu",
            phone = "044-27222100",
            pincode = "631501",
            latitude = 12.8342,
            longitude = 79.7036,
            jurisdiction = "Kanchipuram Temple Zone, Bus Stand Area"
        ),
        PoliceStation(
            id = "awps_cuddalore_01",
            name = "AWPS Cuddalore Town",
            district = "Cuddalore",
            address = "Beach Road, Cuddalore OT, Tamil Nadu",
            phone = "04142-230100",
            pincode = "607001",
            latitude = 11.7480,
            longitude = 79.7714,
            jurisdiction = "Cuddalore OT, Port Area, Manjakuppam"
        ),
        PoliceStation(
            id = "awps_kk_01",
            name = "AWPS Nagercoil (Kanyakumari)",
            district = "Kanyakumari",
            address = "Court Road, Nagercoil, Tamil Nadu",
            phone = "04652-230100",
            pincode = "629001",
            latitude = 8.1833,
            longitude = 77.4119,
            jurisdiction = "Nagercoil Town, Meenakshipuram, Vadasery"
        )
    )

    val districts = listOf(
        "All Districts", "Chennai", "Madurai", "Coimbatore", "Tiruchirappalli",
        "Salem", "Tirunelveli", "Thanjavur", "Vellore", "Erode", "Tiruppur",
        "Kanchipuram", "Cuddalore", "Kanyakumari"
    )
}
