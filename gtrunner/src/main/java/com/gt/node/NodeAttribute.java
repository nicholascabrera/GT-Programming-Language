package com.gt.node;

public enum NodeAttribute {
    A, B, C, D, E, F, G, H, I, J, K, L, M, N;

    public static String getNodeString(NodeAttribute attribute) throws Exception{
        switch (attribute){
            case A:
                return "Blood Burden";
            case B:
                return "Slashing";
            case C:
                return "Bludgeoning";
            case D:
                return "Piercing";
            case E:
                return "Fire";
            case F:
                return "Ice";
            case G:
                return "Acid";
            case H:
                return "Poison";
            case I:
                return "Lightning";
            case J:
                return "Thunder";
            case K:
                return "Radiant";
            case L:
                return "Necrotic";
            case M:
                return "Force";
            case N:
                return "Pyschic";
            default:
                throw new Exception("No such node attribute exists.");
        }
    }

    /**
     * Needs to be completed.
     * @param attribute
     * @param level
     * @return
     * @throws Exception
     */
    public static String getNodeDamage(NodeAttribute attribute, int level) throws Exception{
        switch (attribute){
            case A:
                return (level > 1) ? Integer.toString(level*3) : Integer.toString(level);
            case B:
                return "Slashing";
            case C:
                return "Bludgeoning";
            case D:
                return "Piercing";
            case E:
                return "Fire";
            case F:
                return "Ice";
            case G:
                return "Acid";
            case H:
                return "Poison";
            case I:
                return "Lightning";
            case J:
                return "Thunder";
            case K:
                return "Radiant";
            case L:
                return "Necrotic";
            case M:
                return "Force";
            case N:
                return "Psychic";
            default:
                throw new Exception("No such node attribute exists.");
        }
    }

    public static NodeAttribute getNodeAttribute(String string) throws Exception{
        switch (string) {
            case "A":
                return A;
            case "B":
                return B;
            case "C":
                return C;
            case "D":
                return D;
            case "E":
                return E;
            case "F":
                return F;
            case "G":
                return G;
            case "H":
                return H;
            case "I":
                return I;
            case "J":
                return J;
            case "K":
                return K;
            case "L":
                return L;
            case "M":
                return M;
            case "N":
                return N;
            default:
                throw new Exception("No such node attribute exists.");
        }
    }
}