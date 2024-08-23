package de.petermeissner.restjms;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElements;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "list")
public class JaxbList<T> {
    protected List<T> list;


    public JaxbList(List<T> list) {
        this.list = list;
    }

    @XmlElements({
            @XmlElement(name = "message", type = String.class),
    })
    public List<T> getList() {
        return list;
    }
}

