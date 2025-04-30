package com.scape.store;

import java.util.List;

public class StoreService {
    private final StoreDAO dao = new StoreDAO();

    public int getStoreSizeLimit(String location) {
        return dao.findSizeLimitByLocation(location);
    }

    public String getStoreUniqueIdByLocation(String location) {
        return dao.findStoreUniqueIdByLocation(location);
    }

    public List<String> getAllLocations() {
        return dao.getAllLocations();
    }
    
    public String getLocationByRoomId(String roomId) {
        return dao.getLocationByRoomId(roomId);
    }


}
