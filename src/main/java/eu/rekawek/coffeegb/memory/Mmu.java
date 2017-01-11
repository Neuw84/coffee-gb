package eu.rekawek.coffeegb.memory;

import eu.rekawek.coffeegb.AddressSpace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Mmu implements AddressSpace {

    private static final Logger LOG = LoggerFactory.getLogger(Cartridge.class);

    private static final AddressSpace VOID = new AddressSpace() {
        @Override
        public boolean accepts(int address) {
            return true;
        }

        @Override
        public void setByte(int address, int value) {
            LOG.warn("Writing value {} to void address {}", Integer.toHexString(value), Integer.toHexString(address));
        }

        @Override
        public int getByte(int address) {
            LOG.warn("Reading value from void address {}", Integer.toHexString(address));
            return 0xff;
        }
    };

    private final List<AddressSpace> spaces = new ArrayList<>();

    public Mmu() {
        Ram internalRam = new Ram(0xc000, 0x2000);
        Ram shadowRam = Ram.createShadow(0xe000, 0x1e00, internalRam);
        Ram ffRam = new Ram(0xff80, 0x0080);

        addAddressSpace(internalRam);
        addAddressSpace(shadowRam);
        addAddressSpace(ffRam);
    }

    public void addAddressSpace(AddressSpace space) {
        spaces.add(space);
    }

    @Override
    public boolean accepts(int address) {
        return true;
    }

    @Override
    public void setByte(int address, int value) {
        getSpace(address).setByte(address, value);
    }

    @Override
    public int getByte(int address) {
        return getSpace(address).getByte(address);
    }

    private AddressSpace getSpace(int address) {
        for (AddressSpace s : spaces) {
            if (s.accepts(address)) {
                return s;
            }
        }
        return VOID;
    }

}