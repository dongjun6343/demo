package com.batch.demo.job.file;

import com.batch.demo.job.file.dto.Player;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

public class PlayerFieldSetMapper implements FieldSetMapper<Player> {
    @Override
    public Player mapFieldSet(FieldSet fieldSet) {
        Player player = new Player();

        player.setId(fieldSet.readString(0));
        player.setLastName(fieldSet.readString(1));
        player.setFirstName(fieldSet.readString(2));
        player.setPosition(fieldSet.readString(3));
        player.setBirthYear(fieldSet.readInt(4));
        player.setDebutYear(fieldSet.readInt(5));

        return player;
    }
}
