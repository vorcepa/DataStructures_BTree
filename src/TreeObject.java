public class TreeObject{
    private long binarySequence;

    public TreeObject(String sequence){
        binarySequence = buildBinarySequence(sequence);
    }

    private long buildBinarySequence(String sequence){
        long charConversion = 0;
        long charAdd = 0;
        for (int i = 0; i < sequence.length(); i++){
            switch (sequence.charAt(i)){
                case 'a':
                    charAdd = 0b00;
                    break;
                case 'c':
                    charAdd = 0b01;
                    break;
                case 'g':
                    charAdd = 0b10;
                    break;
                case 't':
                    charAdd = 0b11;
                    break;
            }

            charAdd = charAdd << 2*(sequence.length() - i - 1);
            charConversion = charConversion | charAdd;
        }

        return charConversion;
    }

    public long getSequence(){
        return binarySequence;
    }


    public boolean equals(TreeObject otherObject){
        return this.binarySequence == otherObject.getSequence();
    }
}