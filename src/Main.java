import application.ApplicationController;
import collection.CollectionManager;
import collection.StackCollectionManager;
import collection.data.musicband.MusicBand;
import collection.data.musicband.Person;
import commands.musicband.MinByFrontManCommand;
import commands.musicband.PrintFieldAscendingEstablishmentDateCommand;
import commands.musicband.RemoveAllByFrontManCommand;

import java.time.LocalDateTime;


public class Main {
    public static void main(String... args) {
        CollectionManager<MusicBand> musicBandCollection = new StackCollectionManager<MusicBand>() {
            @Override
            public MusicBand generateNew() {
                return new MusicBand(generateId());
            }
        };


        ApplicationController<MusicBand> applicationController = new ApplicationController<>(musicBandCollection);
        applicationController.addCommand(new RemoveAllByFrontManCommand(musicBandCollection));
        applicationController.addCommand(new MinByFrontManCommand(musicBandCollection));
        applicationController.addCommand(new PrintFieldAscendingEstablishmentDateCommand(musicBandCollection));

        applicationController.run((args.length!=0) ? String.join(" ", args).trim() : null);

    }
}
