String tepy=InputUtil.getString("选择要租的车型:轿车,面包车,巴士，客车");

        // 先显示可租赁车辆
        System.out.println("可租赁车辆列表：");
        vehicleController.displayVehiclesByType(tepy);