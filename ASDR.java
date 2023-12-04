import java.util.List;

public class ASDR implements Parser{
    private int i = 0;
    private boolean err = false;
    private Token PreAnalisis;
    private final List<Token> tokens;

    public ASDR(List<Token> tokens){
        this.tokens = tokens;
        PreAnalisis = this.tokens.get(i);
    }

    @Override
    public boolean parse() {
        Q();
        if( ( PreAnalisis.tipo == TipoToken.EOF ) && !err){
            System.out.println("Consulta correcta");
            return  true;
        }else {
            System.out.println("Hay errores");
        }
        return false;
    }

    //! Q -> select D from T
    private void Q(){
        match(TipoToken.SELECT);
        D();
        match(TipoToken.FROM);
        T();
    
    
    }

    //! D -> distinct P | P
    private void D(){
        if(err) return;

        //! Primera proyección D -> distinct P
        if(PreAnalisis.tipo == TipoToken.DISTINCT){
            match(TipoToken.DISTINCT);
            P();
        //! Segunda proyección D -> P
        }else if (PreAnalisis.tipo == TipoToken.ASTERISCO || PreAnalisis.tipo == TipoToken.IDENTIFICADOR) {
            P();
        }else{
            err = true;
            System.out.println("Se esperaba 'distinct' o '*' o 'id'");
        
        }
    }

    //! P -> * | A
    private void P(){
        if(err) return;

        //! Primera proyección  P -> *
        if(PreAnalisis.tipo == TipoToken.ASTERISCO){
            match(TipoToken.ASTERISCO);
        //! Segunda proyección  P -> A
        }else if(PreAnalisis.tipo == TipoToken.IDENTIFICADOR){
            A();
        }
        else{
            err = true;
            System.out.println("Se esperaba '*' o 'id'");
        }
    }

    //! A -> A2 A1
    private void A(){
        if(err) return;

        A2();
        A1();
    }

    //! A2 -> id A3
    private void A2(){
        if(err) return;

        if(PreAnalisis.tipo == TipoToken.IDENTIFICADOR){
            match(TipoToken.IDENTIFICADOR);
            A3();
        }else{
            err = true;
            System.out.println("Se esperaba 'id'");
        }
    }

    //! A1 -> ,A | Ɛ
    private void A1(){
        if(err) return;

        if(PreAnalisis.tipo == TipoToken.COMA){
            match(TipoToken.COMA);
            A();
        }
    }

    //! A3 -> .id | Ɛ
    private void A3(){
        if(err) return;

        if(PreAnalisis.tipo == TipoToken.PUNTO){
            match(TipoToken.PUNTO);
            match(TipoToken.IDENTIFICADOR);
        }
    }

    //! T -> T2T1
    private void T (){
        if(err) return;

        T2();
        T1();
    }

    //! T1 -> ,T | Ɛ
    private void T1 (){
        if(err) return;

        if ( this.PreAnalisis.tipo == TipoToken.COMA ) {
            match( TipoToken.COMA );
            T();
        }
    }
    
    //! T2 -> idT3
    private void T2 (){
        
        if(err) return;

        if( this.PreAnalisis.tipo == TipoToken.IDENTIFICADOR ) {
            match( TipoToken.IDENTIFICADOR );
            T3();
        }else{
            err = true;
            System.out.println("Se esperaba 'id'");
        }
    }

    //! T3 → id | Ɛ
    private void T3(){
        if(err) return;
        if(PreAnalisis.tipo == TipoToken.IDENTIFICADOR){
            match(TipoToken.IDENTIFICADOR);
        }
    }

    private void match(TipoToken tt){
        if(PreAnalisis.tipo == tt){
            i++;
            PreAnalisis = tokens.get(i);
        }else{
            err = true;
            System.out.println("Error encontrado");
        }
    }
}