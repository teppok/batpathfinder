/* 
    Copyright 2017 Maxpower, Lassi Marttala, lassi.marttala@maxpower.fi

	This file is part of BatPathFinder.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package fi.iki.photon.batmud;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fizzl
 * @brief Maputils locfile reader
 * This class handles the loading of maputils (http://pupunen.net/hg/maputils/)
 * style .loc-files. Current supported loc file version is 4.
 */
public final class LocFile {
    private File file;
    private List<String> lines;
    private List<LocFileRecord> records;

    public List<LocFileRecord> getRecords() {
        return records;
    }

    public void setRecords(List<LocFileRecord> records) {
        this.records = records;
    }
    
    public LocFile() {
        lines = new ArrayList<>();
    }
    public LocFile(String filename) throws IOException {
        this();
        this.open(new File(filename));
    }
    public LocFile(File file) throws IOException {
        this();
        this.open(file);
    }
    public void open(String filename) throws IOException {
        this.open(new File(filename));
    }
    public void open(File file) throws IOException {
        this.file = file;
        if (!this.file.canRead()) {
            throw new IOException("Can not read .loc-file");
        }
    }
    public void load() throws IOException, BPFException {
        InputStream is = new FileInputStream(this.file);
        InputStreamReader isReader =  new InputStreamReader(is, "ISO-8859-1");
        BufferedReader reader = new BufferedReader(isReader);
        String line, currentLine = "";
        boolean continues = false;
        while((line = reader.readLine()) != null) {
            line = line.trim();
            // Ignore commets
            if(line.startsWith("#")) {
                continue;
            }
            // Ignore empty lines
            if(line.equals("")) {
                continue;
            }
            // Lines may be broken to multiple lines by \
            if(continues) {
                currentLine += line;
            }
            else {
                currentLine = line;
            }
            continues = line.endsWith("\\");
            
            if(!continues) {
                this.records.add(this.compile(currentLine));
            }
        }
    }
    
    public LocFileRecord compile(String line) throws BPFException {
        LocFileRecord ret = new LocFileRecord();
        StringBuilder currentComponent = new StringBuilder();
        List<String> components = new ArrayList<>();
        boolean escaping = false;
        
        for(char c : line.toCharArray()) {
            if(escaping) {
                currentComponent.append(c);
                escaping = false;
                continue;
            }
            if(c == '\\') {
                escaping = true;
                continue;
            }
            if(c == ';' || c == '\n') {
                components.add(currentComponent.toString().trim());
                currentComponent = new StringBuilder();
                continue;
            }
            currentComponent.append(c);
        }
        if(components.size() != 8) {
            throw new BPFException("LocFile. Line corrupted. "+line);
        }
        ret.setX(Integer.parseInt(components.get(0)));
        ret.setY(Integer.parseInt(components.get(1)));
        ret.setFlags(components.get(2));
        ret.setNames(components.get(3));
        ret.setCreators(components.get(4));
        ret.setTimestamp(components.get(5));
        ret.setUrl(components.get(6));
        ret.setFreeform(components.get(7));
        return ret;
    }
    
    /**
     * Container for one complete record from .loc-file
     * TODO: names, creators do not obey escaped pipes.
     */
    public class LocFileRecord {
        private int x,y;
        private String flags,timestamp,url,freeform,prettyname;

        private String sanitizeName(String name) {
            String ret = name.toLowerCase();
            ret = ret.replaceAll("[^a-z0-9]", "");
            return ret;
        }
        public String getPrettyname() {
            return prettyname;
        }

        public void setPrettyname(String prettyname) {
            this.prettyname = prettyname;
        }
        private String[] names, creators;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public String getFlags() {
            return flags;
        }

        public void setFlags(String flags) {
            this.flags = flags;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getFreeform() {
            return freeform;
        }

        public void setFreeform(String freeform) {
            this.freeform = freeform;
        }

        public String[] getNames() {
            return names;
        }

        public void setNames(String names) {
            setNames(names.split("|"));
        }

        public void setNames(String[] names) {
            if(names.length > 0) {
                this.setPrettyname(this.sanitizeName(names[0]));
            }
            this.names = names;
        }

        public String[] getCreators() {
            return creators;
        }

        public void setCreators(String[] creators) {
            this.creators = creators;
        }
        public void setCreators(String creators) {
            setCreators(creators.split(creators));
        }
        
    }
}
