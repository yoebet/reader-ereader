package wjy.yo.ereader.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(tableName = "setting")
@Data
@NoArgsConstructor
public class LocalSetting {
    @PrimaryKey
    @NonNull
    private String code;

    private String value;

    private Date updatedAt;
}
