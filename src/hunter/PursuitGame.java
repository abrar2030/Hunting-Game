package hunter;

public class PursuitGame {

    public static void main(String[] args) {
        initiateMainApp();
    }
    
    private static void initiateMainApp() {
        MainApp mainAppInstance = new MainApp();
        mainAppInstance.setVisible(true);
    }
}

