package br.com.virtualab.app.service.tema;

import br.com.virtualab.app.entity.tema.Tema;
import br.com.virtualab.app.service.Abstract.AbstractService;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;

public class TemaFactory extends AbstractService {

    private static final long serialVersionUID = 1L;

    private List<Tema> temas;

    @PostConstruct
    public void init() {
        temas = new ArrayList<Tema>();
        temas.add(new Tema(0, "Afterdark", "afterdark"));
        temas.add(new Tema(1, "Afternoon", "afternoon"));
        temas.add(new Tema(2, "Afterwork", "afterwork"));
        temas.add(new Tema(3, "Aristo", "aristo"));
        temas.add(new Tema(4, "Black-Tie", "black-tie"));
        temas.add(new Tema(5, "Blitzer", "blitzer"));
        temas.add(new Tema(6, "Bluesky", "bluesky"));
        temas.add(new Tema(7, "Bootstrap", "bootstrap"));
        temas.add(new Tema(8, "Casablanca", "casablanca"));
        temas.add(new Tema(9, "Cupertino", "cupertino"));
        temas.add(new Tema(10, "Cruze", "cruze"));
        temas.add(new Tema(11, "Dark-Hive", "dark-hive"));
        temas.add(new Tema(12, "Delta", "delta"));
        temas.add(new Tema(13, "Dot-Luv", "dot-luv"));
        temas.add(new Tema(14, "Eggplant", "eggplant"));
        temas.add(new Tema(15, "Excite-Bike", "excite-bike"));
        temas.add(new Tema(16, "Flick", "flick"));
        temas.add(new Tema(17, "Glass-X", "glass-x"));
        temas.add(new Tema(18, "Home", "home"));
        temas.add(new Tema(19, "Hot-Sneaks", "hot-sneaks"));
        temas.add(new Tema(20, "Humanity", "humanity"));
        temas.add(new Tema(21, "Le-Frog", "le-frog"));
        temas.add(new Tema(22, "Midnight", "midnight"));
        temas.add(new Tema(23, "Mint-Choc", "mint-choc"));
        temas.add(new Tema(24, "Overcast", "overcast"));
        temas.add(new Tema(25, "Pepper-Grinder", "pepper-grinder"));
        temas.add(new Tema(26, "Redmond", "redmond"));
        temas.add(new Tema(27, "Rocket", "rocket"));
        temas.add(new Tema(28, "Sam", "sam"));
        temas.add(new Tema(29, "Smoothness", "smoothness"));
        temas.add(new Tema(30, "South-Street", "south-street"));
        temas.add(new Tema(31, "Start", "start"));
        temas.add(new Tema(32, "Sunny", "sunny"));
        temas.add(new Tema(33, "Swanky-Purse", "swanky-purse"));
        temas.add(new Tema(34, "Trontastic", "trontastic"));
        temas.add(new Tema(35, "UI-Darkness", "ui-darkness"));
        temas.add(new Tema(36, "UI-Lightness", "ui-lightness"));
        temas.add(new Tema(37, "Vader", "vader"));
    }

    public List<Tema> getTemas() {
        return temas;
    }

}
