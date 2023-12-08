import model.Club;
import model.Datasource;
import model.View;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Datasource datasource = new Datasource();
        if(!datasource.open()) {
            System.out.println("Nie mozna otworzyc polaczenia");
            return;
        }
        boolean condition = true;
        int option;
        Scanner scanner = new Scanner(System.in);
        while (condition)
        {
            System.out.println("\nAby wyswietlic widoki wcisnij '1'");
            System.out.println("Aby dodac zawodnika wcisnij '2'");
            System.out.println("Aby usunac zawodnika wcisnij '3'");
            System.out.println("Aby zaktualizowac dane zawodnika wcisnij '4'");
            System.out.println("Aby wyswietlic informacje o klubach wcisnij '5'");
            System.out.println("Ayb wyświetlić dane o zawonikawch klubu '6'");
            System.out.println("Aby zakonczyc dzialanie wcisnij '7'");
            System.out.print("Twoj wybor: ");
            option = scanner.nextInt();
            switch (option) {
                case 1:
                    System.out.println("Aby wyswietlic widok posortowany wzgledem punktow wcisnij '1'");
                    System.out.println("Aby wyswietlic widok posortowany wzgledem asyst wcisnij '2'");
                    System.out.println("Aby wyswietlic widok posortowany wzgledem zbiorek wcisnij '3'");
                    System.out.print("Podaj numer: ");
                    int opt = scanner.nextInt();
                    if(opt == 1){
                        printView(datasource, "points_view");
                    } else if (opt == 2){
                        printView(datasource, "assist_view");
                    } else if(opt == 3){
                        printView(datasource, "rebounds_view");
                    } else {
                        System.out.println("Niepoprawny numer");
                    }
                    break;
                case 2:
                    String name = "Ivica";
                    String surname = "Zubac";
                    String pos = "C";
                    int clubId = 8;
                    String pts = "35.7";
                    String reb = "8.8";
                    String ast = "2.3";
                    datasource.addPlayer(name, surname, pos, clubId);
                    datasource.addStats(pts, ast, reb);
                    break;
                case 3:
                    int playerId = 32;
                    datasource.deleteStats(playerId);
                    datasource.deletePlayer(playerId);
                    break;
                case 4:
                    int playerID = 31;
                    datasource.updateStats("40.2", "2.3", "4.4", playerID);
                    break;
                case 5:
                    printClubs(datasource);
                    break;
                case 6:
                    printPlayers(datasource, "Bulls");
                    break;
                case 7:
                    condition = false;
                    break;
                default:
                    System.out.println("Wprowadzona liczba nie pasuje do zadnego przypadku");
            }
        }
        datasource.close();
    }

    public static void printView(Datasource datasource, String viewName) {
        List<View> views = datasource.queryView(viewName);
        if(views == null) {
            System.out.println("No players");
            return;
        }
        System.out.println("Player Name | Club Name | Points | Assists | Rebounds");
        for(View view : views) {
            System.out.println(view.getPlayerName() + "| " + view.getClub() + "| " + view.getPts() + "| " + view.getAst() + "| " + view.getReb());
        }
    }
    public static void printClubs(Datasource datasource) {
        List<Club> clubs = datasource.clubsList();
        if(clubs == null) {
            System.out.println("No clubs");
            return;
        }
        System.out.println("City Name | Club Name | Division");
        for(Club club : clubs) {
            System.out.println(club.getCityName() + "| " + club.getClubName() + "| " + club.getDivision());
        }
    }
    public static void printPlayers(Datasource datasource , String clubName ) {
        List<View> views = datasource.clubPlayerList(clubName);
        if(views == null) {
            System.out.println("No players");
            return;
        }
        System.out.println("Player Name | Position | Points | Assists | Rebounds");
        for(View view : views) {
            System.out.println(view.getPlayerName() + "| " + view.getPos() + "| " + view.getPts() + "| " + view.getAst() + "| " + view.getReb());
        }
    }
}
