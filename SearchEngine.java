import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

class Handler implements URLHandler {
    // The one bit of state on the server: a number that will be manipulated by
    // various requests.
    ArrayList<String> store = new ArrayList<>();

    public String handleRequest(URI url) {
        if (url.getPath().equals("/")) {
            String everything = "";
            for (int i = 0; i < store.size(); i++) {
                everything += store.get(i) + "\n";
            }
            return everything;
        }
        else if (url.getPath().equals("/search")) {
            String[] parameter = url.getQuery().split("=");
            String searchedThings = "";
            for (String element : store) {
                if (element.contains(parameter[1])) {
                    searchedThings += element + "\n";
                }
            }
            return "Found: \n" + searchedThings;
        }
        else {
            System.out.println("Path: " + url.getPath());
            if (url.getPath().contains("/add")) {
                String[] parameters = url.getQuery().split("=");
                if (parameters[0].equals("s")) {
                    store.add(parameters[1]);
                    return String.format("%s has been added to the store", parameters[1]);
                }
            }
            return "404 Not Found!";
        }
    }
}

class SearchEngine {
    public static void main(String[] args) throws IOException {
        if(args.length == 0){
            System.out.println("Missing port number! Try any number between 1024 to 49151");
            return;
        }

        int port = Integer.parseInt(args[0]);

        Server.start(port, new Handler());
    }
}