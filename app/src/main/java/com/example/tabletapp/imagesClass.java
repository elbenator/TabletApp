package com.example.tabletapp;

import java.util.List;

public class imagesClass {
    String item_id;
    int images;

    public int passthepic(String itemId) {
        this.item_id = itemId;

        switch (itemId){
            // ====== Milktea Images ====
            case "MTCPeaerl":
                images = R.drawable.class_pearl;
                break;
            case "MTCblackf":
                images =  R.drawable.class_black;
                break;
            case "MTCbrownSugarLa":
                images = R.drawable.class_brown;
                break;
            case "MTCcheesecake":
                images=R.drawable.class_cheesecake;
                break;
            case "MTCchocolate":
                images=R.drawable.class_choco;
                break;
            case "MTChazel":
                images=R.drawable.class_hazel;
                break;
            case "MTChokaido":
                images=R.drawable.class_hokkaido;
                break;
            case "MTCmatcha":
                images=R.drawable.class_matcha;
                break;
            case "MTCokinawa":
                images=R.drawable.class_okinawa;
                break;
            case "MTCoreo":
                images=R.drawable.class_oreo;
                break;
            case "MTCredVelvet":
                images=R.drawable.class_redvelvet;
                break;
            case "MTCstrawberry":
                images= R.drawable.class_strawberry;
                break;
            case "MTCwinter":
                images= R.drawable.class_wintermelon;
                break;

            // ====== Cheesecake Milktea Images ====
            case "MTCHblackForest":
                images= R.drawable.ch1_black;
                break;
            case "MTCHbrownSLatte":
                images = R.drawable.ch1_brown;
                break;
            case "MTCHchocoHzl":
                images = R.drawable.ch1_hazelnut;
                break;
            case "MTCHchocolate":
                images= R.drawable.ch1_choco;
                break;
            case "MTCHdoubleCheese":
                images = R.drawable.ch1_doublecheesecake;
                break;
            case "MTCHmatcha":
                images = R.drawable.ch1_matcha;
                break;
            case "MTCHokinawa":
                images = R.drawable.ch1_okinawa;
                break;
            case "MTCHoreoDoubleCheese":
                images = R.drawable.ch1_double_oreo;
                break;
            case "MTCHoreoNcream":
                images = R.drawable.ch1_oreo;
                break;
            case "MTCHredVelvet":
                images = R.drawable.ch1_redvelvet;
                break;
            case "MTCHstrawberry":
                images = R.drawable.ch1_strawberry;
                break;
            case "MTCHwintermelon":
                images = R.drawable.ch1_wintermelon;
                break;

        // ====== Noodles Images ====
            case "FNallMeat":
                images = R.drawable.fn_allmeat;
                break;
            case "FNbacon":
                images = R.drawable.fn_bacon;
                break;
            case "FNbeefTeri":
                images = R.drawable.fn_beef;
                break;
            case "FNshanghai":
                images = R.drawable.fn_shanghai;
                break;
            case "FNsharksfin":
                images = R.drawable.fn_sharksfin;
                break;
            case "FNsiomai":
                images = R.drawable.fn_siomai;
                break;
            case "FNtotkatsu":
                images = R.drawable.fn_tongkatsu;
                break;


            // ====== Ricebowl Images ====
            case "RBAllmeat":
                images = R.drawable.rb1_allmeat;
                break;
            case "RBchzBacon":
                images = R.drawable.rb1_cheesy_bacon;
                break;
            case "RBchzNugget":
                images = R.drawable.rb1_cheesy_nuggets;
                break;
            case "RBchzPorktonk":
                images = R.drawable.rb1_cheesy_tongkatsu;
                break;
            case "RBchzTapa":
                images = R.drawable.rb1_cheesy_tapa;
                break;
            case "RBporkSisig":
                images = R.drawable.rb1_sisig;
                break;
            case "RBshanhai":
                images= R.drawable.rb1_shanghai;
                break;
            case "RBsharksfin":
                images = R.drawable.rb1_sharksfin;
                break;
            case "RBsiomai":
                images= R.drawable.rb1_siomai;
                break;


            //====== ALacarte Images =======
            case "ALCSNuggets":
                //images.add(R.drawable.ala_carte_nuggets);
                images = R.drawable.nuggets2;
                break;
            case "ALCSporksisig":
               // images.add(R.drawable.ala_carte_sisig);
                images = R.drawable.sigsig2;
                break;
            case "ALCSshanghai":
                //images.add(R.drawable.ala_carte_shanghai);
                images = R.drawable.ala_carte_shanghai;
                break;
            case "ALCSsharks":
                //images.add(R.drawable.ala_carte_sharksfin);
                images = R.drawable.ala_carte_sharksfin;
                break;
            case "ALCSsiomai":
                //images.add(R.drawable.ala_carte_siomai);
                images = R.drawable.ala_carte_siomai;
                break;
            default:
               // images.add(R.drawable.ic_baseline_fastfood);
                images = R.drawable.ic_baseline_fastfood;
        }
        return images;
    }

    public String something(String blah){
        this.item_id = blah;
        item_id += "na add";
        return item_id;
    }
}
