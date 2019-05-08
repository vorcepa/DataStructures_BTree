public class TreeObject{
    private long binarySequence;
    private String characterSequence;
    private int duplicates;

    public TreeObject(String sequence){
        characterSequence = sequence;
        binarySequence = buildBinarySequence(sequence);
        duplicates = 0;
    }

    public TreeObject(long sequence, int duplicates, int sequenceLength){
        binarySequence = sequence;
        this.duplicates = duplicates;
        characterSequence = buildCharacterSequence(sequence, sequenceLength);
    }

    private String buildCharacterSequence(long sequence, int sequenceLength){
        StringBuilder retVal = new StringBuilder();
        while (sequenceLength > 0){
            if ((sequence & 0b11) == 0b11){
                retVal.append('t');
            }
            else if ((sequence & 0b11) == 0b10){
                retVal.append('g');
            }
            else if ((sequence & 0b11) == 0b01){
                retVal.append('c');
            }
            else{
                retVal.append('a');
            }

            sequence = sequence >> 2;
            sequenceLength--;
        }

        return retVal.reverse().toString();
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

    public int getDuplicates(){
        return duplicates;
    }

    public void incrementDuplicates(){
        duplicates++;
    }

    public String toString(){
        return characterSequence;
    }

    public String binaryToString(){
        StringBuilder retVal = new StringBuilder();
        for (int i = 0; i < characterSequence.length(); i++){
            if (characterSequence.charAt(i) == 'a'){
                retVal.append("00");
            }
            else{
                break;
            }
        }

        return retVal.toString() + Long.toBinaryString(binarySequence);
    }
}