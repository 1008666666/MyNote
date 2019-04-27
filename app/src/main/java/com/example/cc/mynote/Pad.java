package com.example.cc.mynote;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;

import java.util.Date;

@Entity(
        nameInDb = "Pads",
        indexes = {
                @Index(value = "createtime DESC")
        }
)

public class Pad {
    @Id(autoincrement = true)
    private Long id;

    private String title;

    @Property(nameInDb = "mycontent")
    @NotNull
    private String content;

    @Index(name = "time_index",unique = true)
    private String createtime;

@Generated(hash = 27053005)
public Pad(Long id, String title, @NotNull String content, String createtime) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.createtime = createtime;
}

@Generated(hash = 54497821)
public Pad() {
}

public Long getId() {
    return this.id;
}

public void setId(Long id) {
    this.id = id;
}

public String getTitle() {
    return this.title;
}

public void setTitle(String title) {
    this.title = title;
}

public String getContent() {
    return this.content;
}

public void setContent(String content) {
    this.content = content;
}

public String getCreatetime() {
    return this.createtime;
}

public void setCreatetime(String createtime) {
    this.createtime = createtime;
}


}
