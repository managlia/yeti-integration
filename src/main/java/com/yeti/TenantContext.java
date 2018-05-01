package com.yeti;

public class TenantContext {

    private static ThreadLocal<Integer> currentTenant = new ThreadLocal<>();
    private static ThreadLocal<Integer> currentUser = new ThreadLocal<>();

    public static void setCurrentTenant(Integer tenant) {
        currentTenant.set(tenant);
    }
    
    public static Integer getCurrentTenant() {
        return currentTenant.get();
    }
    
    public static void setCurrentUser(Integer userId) {
    	currentUser.set(userId);
    }
    
    public static Integer getCurrentUser() {
        return currentUser.get();
    }
    
    public static void clear() {
        currentTenant.set(null);
        currentUser.set(null);
    }
}