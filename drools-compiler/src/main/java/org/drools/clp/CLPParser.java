// $ANTLR 3.0b7 D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g 2007-03-22 14:11:48

	package org.drools.clp;
	import java.util.List;
	import java.util.ArrayList;
	import java.util.Iterator;
	import java.util.HashMap;	
	import java.util.StringTokenizer;
	import org.drools.lang.descr.*;
	import org.drools.compiler.SwitchingCommonTokenStream;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class CLPParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "LEFT_PAREN", "DEFRULE", "NAME", "STRING", "RIGHT_PAREN", "SALIENCE", "INT", "AND", "OR", "NOT", "EXISTS", "TEST", "VAR", "ASSIGN_OP", "AMPERSAND", "PIPE", "TILDE", "COLON", "EQUALS", "MODIFY", "FLOAT", "BOOL", "NULL", "EOL", "WS", "DECLARE", "EscapeSequence", "HexDigit", "UnicodeEscape", "OctalEscape", "SYMBOL", "SH_STYLE_SINGLE_LINE_COMMENT", "C_STYLE_SINGLE_LINE_COMMENT", "LEFT_SQUARE", "RIGHT_SQUARE", "LEFT_CURLY", "RIGHT_CURLY", "MULTI_LINE_COMMENT", "'=>'"
    };
    public static final int EXISTS=14;
    public static final int LEFT_PAREN=4;
    public static final int RIGHT_CURLY=40;
    public static final int BOOL=25;
    public static final int DEFRULE=5;
    public static final int HexDigit=31;
    public static final int WS=28;
    public static final int STRING=7;
    public static final int FLOAT=24;
    public static final int TILDE=20;
    public static final int OR=12;
    public static final int PIPE=19;
    public static final int EQUALS=22;
    public static final int VAR=16;
    public static final int ASSIGN_OP=17;
    public static final int UnicodeEscape=32;
    public static final int AND=11;
    public static final int EscapeSequence=30;
    public static final int INT=10;
    public static final int EOF=-1;
    public static final int EOL=27;
    public static final int NULL=26;
    public static final int SYMBOL=34;
    public static final int LEFT_SQUARE=37;
    public static final int MODIFY=23;
    public static final int COLON=21;
    public static final int SH_STYLE_SINGLE_LINE_COMMENT=35;
    public static final int OctalEscape=33;
    public static final int SALIENCE=9;
    public static final int MULTI_LINE_COMMENT=41;
    public static final int TEST=15;
    public static final int AMPERSAND=18;
    public static final int NAME=6;
    public static final int DECLARE=29;
    public static final int RIGHT_PAREN=8;
    public static final int NOT=13;
    public static final int LEFT_CURLY=39;
    public static final int RIGHT_SQUARE=38;
    public static final int C_STYLE_SINGLE_LINE_COMMENT=36;

        public CLPParser(TokenStream input) {
            super(input);
        }
        

    public String[] getTokenNames() { return tokenNames; }
    public String getGrammarFileName() { return "D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g"; }

    
    	private PackageDescr packageDescr;
    	private List errors = new ArrayList();
    	private String source = "unknown";
    	private int lineOffset = 0;
    	private DescrFactory factory = new DescrFactory();
    	private boolean parserDebug = false;
    	private FunctionRegistry functionRegistry;
    	
        FunctionRegistry factoryRegistry;
    	
    	public void setFunctionRegistry(FunctionRegistry functionRegistry) {
    		this.functionRegistry = functionRegistry;
    	}
    	
    	public void setParserDebug(boolean parserDebug) {
    		this.parserDebug = parserDebug;
    	}		
    	
    	public void debug(String message) {
    		if ( parserDebug ) 
    			System.err.println( "drl parser: " + message );
    	}
    	
    	public void setSource(String source) {
    		this.source = source;
    	}
    	public DescrFactory getFactory() {
    		return factory;
    	}	
    
    	public String getSource() {
    		return this.source;
    	}
    	
    	public PackageDescr getPackageDescr() {
    		return packageDescr;
    	}
    	
    	private int offset(int line) {
    		return line + lineOffset;
    	}
    	
    	/**
    	 * This will set the offset to record when reparsing. Normally is zero of course 
    	 */
    	public void setLineOffset(int i) {
    	 	this.lineOffset = i;
    	}
    	
    	private String getString(Token token) {
    		String orig = token.getText();
    		return orig.substring( 1, orig.length() -1 );
    	}
    	
    	public void reportError(RecognitionException ex) {
    	        // if we've already reported an error and have not matched a token
                    // yet successfully, don't report any errors.
                    if ( errorRecovery ) {
                            return;
                    }
                    errorRecovery = true;
    
    		ex.line = offset(ex.line); //add the offset if there is one
    		errors.add( ex ); 
    	}
         	
         	/** return the raw RecognitionException errors */
         	public List getErrors() {
         		return errors;
         	}
         	
         	/** Return a list of pretty strings summarising the errors */
         	public List getErrorMessages() {
         		List messages = new ArrayList();
     		for ( Iterator errorIter = errors.iterator() ; errorIter.hasNext() ; ) {
         	     		messages.add( createErrorMessage( (RecognitionException) errorIter.next() ) );
         	     	}
         	     	return messages;
         	}
         	
         	/** return true if any parser errors were accumulated */
         	public boolean hasErrors() {
      		return ! errors.isEmpty();
         	}
         	
         	/** This will take a RecognitionException, and create a sensible error message out of it */
         	public String createErrorMessage(RecognitionException e)
            {
    		StringBuffer message = new StringBuffer();		
                    message.append( source + ":"+e.line+":"+e.charPositionInLine+" ");
                    if ( e instanceof MismatchedTokenException ) {
                            MismatchedTokenException mte = (MismatchedTokenException)e;
                            message.append("mismatched token: "+
                                                               e.token+
                                                               "; expecting type "+
                                                               tokenNames[mte.expecting]);
                    }
                    else if ( e instanceof MismatchedTreeNodeException ) {
                            MismatchedTreeNodeException mtne = (MismatchedTreeNodeException)e;
                            message.append("mismatched tree node: "+
                                                               mtne.foundNode+
                                                               "; expecting type "+
                                                               tokenNames[mtne.expecting]);
                    }
                    else if ( e instanceof NoViableAltException ) {
                            NoViableAltException nvae = (NoViableAltException)e;
    			message.append( "Unexpected token '" + e.token.getText() + "'" );
                            /*
                            message.append("decision=<<"+nvae.grammarDecisionDescription+">>"+
                                                               " state "+nvae.stateNumber+
                                                               " (decision="+nvae.decisionNumber+
                                                               ") no viable alt; token="+
                                                               e.token);
                                                               */
                    }
                    else if ( e instanceof EarlyExitException ) {
                            EarlyExitException eee = (EarlyExitException)e;
                            message.append("required (...)+ loop (decision="+
                                                               eee.decisionNumber+
                                                               ") did not match anything; token="+
                                                               e.token);
                    }
                    else if ( e instanceof MismatchedSetException ) {
                            MismatchedSetException mse = (MismatchedSetException)e;
                            message.append("mismatched token '"+
                                                               e.token+
                                                               "' expecting set "+mse.expecting);
                    }
                    else if ( e instanceof MismatchedNotSetException ) {
                            MismatchedNotSetException mse = (MismatchedNotSetException)e;
                            message.append("mismatched token '"+
                                                               e.token+
                                                               "' expecting set "+mse.expecting);
                    }
                    else if ( e instanceof FailedPredicateException ) {
                            FailedPredicateException fpe = (FailedPredicateException)e;
                            message.append("rule "+fpe.ruleName+" failed predicate: {"+
                                                               fpe.predicateText+"}?");
                    } else if (e instanceof GeneralParseException) {
    			message.append(" " + e.getMessage());
    		}
                   	return message.toString();
            }   
            
            void checkTrailingSemicolon(String text, int line) {
            	if (text.trim().endsWith( ";" ) ) {
            		this.errors.add( new GeneralParseException( "Trailing semi-colon not allowed", offset(line) ) );
            	}
            }
          



    // $ANTLR start deffunction
    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:223:1: deffunction : ;
    public void deffunction() throws RecognitionException {
        try {
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:225:2: ()
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:225:2: 
            {
            }

        }
        finally {
        }
        return ;
    }
    // $ANTLR end deffunction


    // $ANTLR start defrule
    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:227:1: defrule returns [RuleDescr rule] : loc= LEFT_PAREN DEFRULE ruleName= NAME documentation= STRING ruleAttribute[rule] ( ce[lhs] )* '=>' engine= rhs RIGHT_PAREN ;
    public RuleDescr defrule() throws RecognitionException {
        RuleDescr rule = null;

        Token loc=null;
        Token ruleName=null;
        Token documentation=null;
        ExecutionEngine engine = null;


         
        	        rule = null; 
        	        AndDescr lhs = null;
        	        ColumnDescr colum = null;
        	        AttributeDescr module = null;	        
        	      
        try {
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:234:4: (loc= LEFT_PAREN DEFRULE ruleName= NAME documentation= STRING ruleAttribute[rule] ( ce[lhs] )* '=>' engine= rhs RIGHT_PAREN )
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:234:4: loc= LEFT_PAREN DEFRULE ruleName= NAME documentation= STRING ruleAttribute[rule] ( ce[lhs] )* '=>' engine= rhs RIGHT_PAREN
            {
            loc=(Token)input.LT(1);
            match(input,LEFT_PAREN,FOLLOW_LEFT_PAREN_in_defrule68); 
            match(input,DEFRULE,FOLLOW_DEFRULE_in_defrule76); 
            ruleName=(Token)input.LT(1);
            match(input,NAME,FOLLOW_NAME_in_defrule80); 
             	  			  		
            	  		debug( "start rule: " + ruleName.getText() );
            	  		String ruleStr = ruleName.getText();
            
            	        if ( ruleStr.indexOf("::") >= 0 ) {
            	            String mod = ruleStr.substring(0, ruleStr.indexOf("::"));
            	            ruleStr = ruleStr.substring(ruleStr.indexOf("::")+2);
            				module = new AttributeDescr( "agenda-group", mod );
            				module.setLocation( offset(ruleName.getLine()), ruleName.getCharPositionInLine() );
            				module.setStartCharacter( ((CommonToken)ruleName).getStartIndex() );
            				module.setEndCharacter( ((CommonToken)ruleName).getStopIndex() );
            			}
            		    
            		    rule = new RuleDescr( ruleStr, null ); 
            		    if( module != null ) {
            		    	rule.addAttribute( module );
            		    }
            		        
            			rule.setLocation( offset(loc.getLine()), loc.getCharPositionInLine() );
            			rule.setStartCharacter( ((CommonToken)loc).getStartIndex() ); 
            		
            			// not sure how you define where a LHS starts in clips, so just putting it here for now
              	        	lhs = new AndDescr(); 
            	  	        rule.setLhs( lhs ); 
               		        lhs.setLocation( offset(loc.getLine()), loc.getCharPositionInLine() );
            			lhs.setStartCharacter( ((CommonToken)loc).getStartIndex() );				
            		
            documentation=(Token)input.LT(1);
            match(input,STRING,FOLLOW_STRING_in_defrule92); 
            
            	    	// do nothing here for now
            		
            pushFollow(FOLLOW_ruleAttribute_in_defrule98);
            ruleAttribute(rule);
            _fsp--;

            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:269:3: ( ce[lhs] )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);
                if ( (LA1_0==LEFT_PAREN||LA1_0==VAR) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:269:3: ce[lhs]
            	    {
            	    pushFollow(FOLLOW_ce_in_defrule106);
            	    ce(lhs);
            	    _fsp--;


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            match(input,42,FOLLOW_42_in_defrule115); 
            pushFollow(FOLLOW_rhs_in_defrule124);
            engine=rhs();
            _fsp--;

             rule.setConsequence( engine ); 
            match(input,RIGHT_PAREN,FOLLOW_RIGHT_PAREN_in_defrule133); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return rule;
    }
    // $ANTLR end defrule


    // $ANTLR start ruleAttribute
    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:279:1: ruleAttribute[RuleDescr rule] : ( LEFT_PAREN 'declare' ( LEFT_PAREN d= salience RIGHT_PAREN )? RIGHT_PAREN )? ;
    public void ruleAttribute(RuleDescr rule) throws RecognitionException {
        AttributeDescr d = null;


        try {
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:281:3: ( ( LEFT_PAREN 'declare' ( LEFT_PAREN d= salience RIGHT_PAREN )? RIGHT_PAREN )? )
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:281:3: ( LEFT_PAREN 'declare' ( LEFT_PAREN d= salience RIGHT_PAREN )? RIGHT_PAREN )?
            {
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:281:3: ( LEFT_PAREN 'declare' ( LEFT_PAREN d= salience RIGHT_PAREN )? RIGHT_PAREN )?
            int alt3=2;
            int LA3_0 = input.LA(1);
            if ( (LA3_0==LEFT_PAREN) ) {
                int LA3_1 = input.LA(2);
                if ( (LA3_1==DECLARE) ) {
                    alt3=1;
                }
            }
            switch (alt3) {
                case 1 :
                    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:281:5: LEFT_PAREN 'declare' ( LEFT_PAREN d= salience RIGHT_PAREN )? RIGHT_PAREN
                    {
                    match(input,LEFT_PAREN,FOLLOW_LEFT_PAREN_in_ruleAttribute150); 
                    match(input,DECLARE,FOLLOW_DECLARE_in_ruleAttribute152); 
                    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:282:4: ( LEFT_PAREN d= salience RIGHT_PAREN )?
                    int alt2=2;
                    int LA2_0 = input.LA(1);
                    if ( (LA2_0==LEFT_PAREN) ) {
                        alt2=1;
                    }
                    switch (alt2) {
                        case 1 :
                            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:282:6: LEFT_PAREN d= salience RIGHT_PAREN
                            {
                            match(input,LEFT_PAREN,FOLLOW_LEFT_PAREN_in_ruleAttribute159); 
                            pushFollow(FOLLOW_salience_in_ruleAttribute163);
                            d=salience();
                            _fsp--;

                             rule.addAttribute( d ); 
                            match(input,RIGHT_PAREN,FOLLOW_RIGHT_PAREN_in_ruleAttribute167); 

                            }
                            break;

                    }

                    match(input,RIGHT_PAREN,FOLLOW_RIGHT_PAREN_in_ruleAttribute174); 

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end ruleAttribute


    // $ANTLR start salience
    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:286:1: salience returns [AttributeDescr d ] : loc= SALIENCE i= INT ;
    public AttributeDescr salience() throws RecognitionException {
        AttributeDescr d = null;

        Token loc=null;
        Token i=null;

        
        		d = null;
        	
        try {
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:291:3: (loc= SALIENCE i= INT )
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:291:3: loc= SALIENCE i= INT
            {
            loc=(Token)input.LT(1);
            match(input,SALIENCE,FOLLOW_SALIENCE_in_salience204); 
            i=(Token)input.LT(1);
            match(input,INT,FOLLOW_INT_in_salience208); 
            
            			d = new AttributeDescr( "salience", i.getText() );
            			d.setLocation( offset(loc.getLine()), loc.getCharPositionInLine() );
            			d.setStartCharacter( ((CommonToken)loc).getStartIndex() );
            			d.setEndCharacter( ((CommonToken)i).getStopIndex() );
            		

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return d;
    }
    // $ANTLR end salience


    // $ANTLR start ce
    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:301:1: ce[ConditionalElementDescr in_ce] : ( and_ce[in_ce] | or_ce[in_ce] | not_ce[in_ce] | exists_ce[in_ce] | eval_ce[in_ce] | normal_pattern[in_ce] | bound_pattern[in_ce] ) ;
    public void ce(ConditionalElementDescr in_ce) throws RecognitionException {
        try {
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:302:4: ( ( and_ce[in_ce] | or_ce[in_ce] | not_ce[in_ce] | exists_ce[in_ce] | eval_ce[in_ce] | normal_pattern[in_ce] | bound_pattern[in_ce] ) )
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:302:4: ( and_ce[in_ce] | or_ce[in_ce] | not_ce[in_ce] | exists_ce[in_ce] | eval_ce[in_ce] | normal_pattern[in_ce] | bound_pattern[in_ce] )
            {
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:302:4: ( and_ce[in_ce] | or_ce[in_ce] | not_ce[in_ce] | exists_ce[in_ce] | eval_ce[in_ce] | normal_pattern[in_ce] | bound_pattern[in_ce] )
            int alt4=7;
            int LA4_0 = input.LA(1);
            if ( (LA4_0==LEFT_PAREN) ) {
                switch ( input.LA(2) ) {
                case TEST:
                    alt4=5;
                    break;
                case OR:
                    alt4=2;
                    break;
                case NAME:
                    alt4=6;
                    break;
                case NOT:
                    alt4=3;
                    break;
                case AND:
                    alt4=1;
                    break;
                case EXISTS:
                    alt4=4;
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("302:4: ( and_ce[in_ce] | or_ce[in_ce] | not_ce[in_ce] | exists_ce[in_ce] | eval_ce[in_ce] | normal_pattern[in_ce] | bound_pattern[in_ce] )", 4, 1, input);

                    throw nvae;
                }

            }
            else if ( (LA4_0==VAR) ) {
                alt4=7;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("302:4: ( and_ce[in_ce] | or_ce[in_ce] | not_ce[in_ce] | exists_ce[in_ce] | eval_ce[in_ce] | normal_pattern[in_ce] | bound_pattern[in_ce] )", 4, 0, input);

                throw nvae;
            }
            switch (alt4) {
                case 1 :
                    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:302:8: and_ce[in_ce]
                    {
                    pushFollow(FOLLOW_and_ce_in_ce234);
                    and_ce(in_ce);
                    _fsp--;


                    }
                    break;
                case 2 :
                    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:303:7: or_ce[in_ce]
                    {
                    pushFollow(FOLLOW_or_ce_in_ce244);
                    or_ce(in_ce);
                    _fsp--;


                    }
                    break;
                case 3 :
                    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:304:7: not_ce[in_ce]
                    {
                    pushFollow(FOLLOW_not_ce_in_ce253);
                    not_ce(in_ce);
                    _fsp--;


                    }
                    break;
                case 4 :
                    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:305:7: exists_ce[in_ce]
                    {
                    pushFollow(FOLLOW_exists_ce_in_ce262);
                    exists_ce(in_ce);
                    _fsp--;


                    }
                    break;
                case 5 :
                    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:306:8: eval_ce[in_ce]
                    {
                    pushFollow(FOLLOW_eval_ce_in_ce276);
                    eval_ce(in_ce);
                    _fsp--;


                    }
                    break;
                case 6 :
                    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:307:7: normal_pattern[in_ce]
                    {
                    pushFollow(FOLLOW_normal_pattern_in_ce290);
                    normal_pattern(in_ce);
                    _fsp--;


                    }
                    break;
                case 7 :
                    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:308:7: bound_pattern[in_ce]
                    {
                    pushFollow(FOLLOW_bound_pattern_in_ce299);
                    bound_pattern(in_ce);
                    _fsp--;


                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end ce


    // $ANTLR start rhs
    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:312:1: rhs returns [ExecutionEngine engine] : ( (fc= modify_function[context] | fc= function[context] ) )* ;
    public ExecutionEngine rhs() throws RecognitionException {
        ExecutionEngine engine = null;

        FunctionCaller fc = null;


        
        	        engine = new BlockExecutionEngine();
        			ExecutionBuildContext context = new ExecutionBuildContext( engine );  	
        	
        try {
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:319:4: ( ( (fc= modify_function[context] | fc= function[context] ) )* )
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:319:4: ( (fc= modify_function[context] | fc= function[context] ) )*
            {
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:319:4: ( (fc= modify_function[context] | fc= function[context] ) )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);
                if ( (LA6_0==LEFT_PAREN) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:320:4: (fc= modify_function[context] | fc= function[context] )
            	    {
            	    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:320:4: (fc= modify_function[context] | fc= function[context] )
            	    int alt5=2;
            	    int LA5_0 = input.LA(1);
            	    if ( (LA5_0==LEFT_PAREN) ) {
            	        int LA5_1 = input.LA(2);
            	        if ( (LA5_1==NAME) ) {
            	            alt5=2;
            	        }
            	        else if ( (LA5_1==MODIFY) ) {
            	            alt5=1;
            	        }
            	        else {
            	            NoViableAltException nvae =
            	                new NoViableAltException("320:4: (fc= modify_function[context] | fc= function[context] )", 5, 1, input);

            	            throw nvae;
            	        }
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("320:4: (fc= modify_function[context] | fc= function[context] )", 5, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt5) {
            	        case 1 :
            	            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:321:4: fc= modify_function[context]
            	            {
            	            pushFollow(FOLLOW_modify_function_in_rhs343);
            	            fc=modify_function(context);
            	            _fsp--;


            	            }
            	            break;
            	        case 2 :
            	            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:323:4: fc= function[context]
            	            {
            	            pushFollow(FOLLOW_function_in_rhs356);
            	            fc=function(context);
            	            _fsp--;


            	            }
            	            break;

            	    }

            	     System.out.println( "addfunction" + fc.getName() ); context.addFunction( fc ); 

            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return engine;
    }
    // $ANTLR end rhs


    // $ANTLR start and_ce
    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:328:1: and_ce[ConditionalElementDescr in_ce] : LEFT_PAREN AND ( ce[andDescr] )+ RIGHT_PAREN ;
    public void and_ce(ConditionalElementDescr in_ce) throws RecognitionException {
        
                AndDescr andDescr= null;        
            
        try {
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:332:4: ( LEFT_PAREN AND ( ce[andDescr] )+ RIGHT_PAREN )
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:332:4: LEFT_PAREN AND ( ce[andDescr] )+ RIGHT_PAREN
            {
            match(input,LEFT_PAREN,FOLLOW_LEFT_PAREN_in_and_ce393); 
            match(input,AND,FOLLOW_AND_in_and_ce398); 
            
            	    	andDescr = new AndDescr();
            			in_ce.addDescr( andDescr );
            		
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:337:3: ( ce[andDescr] )+
            int cnt7=0;
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);
                if ( (LA7_0==LEFT_PAREN||LA7_0==VAR) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:337:3: ce[andDescr]
            	    {
            	    pushFollow(FOLLOW_ce_in_and_ce404);
            	    ce(andDescr);
            	    _fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt7 >= 1 ) break loop7;
                        EarlyExitException eee =
                            new EarlyExitException(7, input);
                        throw eee;
                }
                cnt7++;
            } while (true);

            match(input,RIGHT_PAREN,FOLLOW_RIGHT_PAREN_in_and_ce413); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end and_ce


    // $ANTLR start or_ce
    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:341:1: or_ce[ConditionalElementDescr in_ce] : LEFT_PAREN OR ( ce[orDescr] )+ RIGHT_PAREN ;
    public void or_ce(ConditionalElementDescr in_ce) throws RecognitionException {
        
                OrDescr orDescr= null;         
            
        try {
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:345:4: ( LEFT_PAREN OR ( ce[orDescr] )+ RIGHT_PAREN )
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:345:4: LEFT_PAREN OR ( ce[orDescr] )+ RIGHT_PAREN
            {
            match(input,LEFT_PAREN,FOLLOW_LEFT_PAREN_in_or_ce441); 
            match(input,OR,FOLLOW_OR_in_or_ce446); 
            
            	    	orDescr = new OrDescr();
            			in_ce.addDescr( orDescr );
            		
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:350:3: ( ce[orDescr] )+
            int cnt8=0;
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);
                if ( (LA8_0==LEFT_PAREN||LA8_0==VAR) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:350:3: ce[orDescr]
            	    {
            	    pushFollow(FOLLOW_ce_in_or_ce452);
            	    ce(orDescr);
            	    _fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt8 >= 1 ) break loop8;
                        EarlyExitException eee =
                            new EarlyExitException(8, input);
                        throw eee;
                }
                cnt8++;
            } while (true);

            match(input,RIGHT_PAREN,FOLLOW_RIGHT_PAREN_in_or_ce461); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end or_ce


    // $ANTLR start not_ce
    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:354:1: not_ce[ConditionalElementDescr in_ce] : LEFT_PAREN NOT ce[notDescr] RIGHT_PAREN ;
    public void not_ce(ConditionalElementDescr in_ce) throws RecognitionException {
        
                NotDescr notDescr= null;         
            
        try {
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:358:4: ( LEFT_PAREN NOT ce[notDescr] RIGHT_PAREN )
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:358:4: LEFT_PAREN NOT ce[notDescr] RIGHT_PAREN
            {
            match(input,LEFT_PAREN,FOLLOW_LEFT_PAREN_in_not_ce489); 
            match(input,NOT,FOLLOW_NOT_in_not_ce494); 
            
            			notDescr = new NotDescr();
            		    in_ce.addDescr( notDescr );
            		
            pushFollow(FOLLOW_ce_in_not_ce500);
            ce(notDescr);
            _fsp--;

            match(input,RIGHT_PAREN,FOLLOW_RIGHT_PAREN_in_not_ce508); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end not_ce


    // $ANTLR start exists_ce
    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:367:1: exists_ce[ConditionalElementDescr in_ce] : LEFT_PAREN EXISTS ce[existsDescr] RIGHT_PAREN ;
    public void exists_ce(ConditionalElementDescr in_ce) throws RecognitionException {
        
                ExistsDescr existsDescr= null;        
            
        try {
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:371:4: ( LEFT_PAREN EXISTS ce[existsDescr] RIGHT_PAREN )
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:371:4: LEFT_PAREN EXISTS ce[existsDescr] RIGHT_PAREN
            {
            match(input,LEFT_PAREN,FOLLOW_LEFT_PAREN_in_exists_ce537); 
            match(input,EXISTS,FOLLOW_EXISTS_in_exists_ce542); 
            
            		    existsDescr = new ExistsDescr();
            		    in_ce.addDescr( existsDescr );
            		
            pushFollow(FOLLOW_ce_in_exists_ce548);
            ce(existsDescr);
            _fsp--;

            match(input,RIGHT_PAREN,FOLLOW_RIGHT_PAREN_in_exists_ce556); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end exists_ce


    // $ANTLR start eval_ce
    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:380:1: eval_ce[ConditionalElementDescr in_ce] : LEFT_PAREN TEST fc= function[context] RIGHT_PAREN ;
    public void eval_ce(ConditionalElementDescr in_ce) throws RecognitionException {
        FunctionCaller fc = null;


        
                EvalDescr evalDescr= null;    
           		ExecutionEngine engine = new CLPEval();     
        		ExecutionBuildContext context = new ExecutionBuildContext( engine );   		         
            
        try {
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:386:4: ( LEFT_PAREN TEST fc= function[context] RIGHT_PAREN )
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:386:4: LEFT_PAREN TEST fc= function[context] RIGHT_PAREN
            {
            match(input,LEFT_PAREN,FOLLOW_LEFT_PAREN_in_eval_ce584); 
            match(input,TEST,FOLLOW_TEST_in_eval_ce589); 
            
            		    evalDescr = new EvalDescr();
            		    in_ce.addDescr( evalDescr );
            		
            pushFollow(FOLLOW_function_in_eval_ce597);
            fc=function(context);
            _fsp--;

            					
            		    engine.addFunction( fc );		
            			evalDescr.setContent( engine );			
            		
            match(input,RIGHT_PAREN,FOLLOW_RIGHT_PAREN_in_eval_ce608); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end eval_ce


    // $ANTLR start normal_pattern
    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:398:1: normal_pattern[ConditionalElementDescr in_ce] : LEFT_PAREN name= NAME ( field_constriant[column] )* RIGHT_PAREN ;
    public void normal_pattern(ConditionalElementDescr in_ce) throws RecognitionException {
        Token name=null;

        
                ColumnDescr column = null;
            
        try {
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:402:4: ( LEFT_PAREN name= NAME ( field_constriant[column] )* RIGHT_PAREN )
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:402:4: LEFT_PAREN name= NAME ( field_constriant[column] )* RIGHT_PAREN
            {
            match(input,LEFT_PAREN,FOLLOW_LEFT_PAREN_in_normal_pattern636); 
            name=(Token)input.LT(1);
            match(input,NAME,FOLLOW_NAME_in_normal_pattern643); 
            
            			column = new ColumnDescr(name.getText());
            			in_ce.addDescr( column );
            		
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:407:3: ( field_constriant[column] )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);
                if ( (LA9_0==LEFT_PAREN) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:407:3: field_constriant[column]
            	    {
            	    pushFollow(FOLLOW_field_constriant_in_normal_pattern649);
            	    field_constriant(column);
            	    _fsp--;


            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);

            match(input,RIGHT_PAREN,FOLLOW_RIGHT_PAREN_in_normal_pattern659); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end normal_pattern


    // $ANTLR start bound_pattern
    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:413:1: bound_pattern[ConditionalElementDescr in_ce] : var= VAR ASSIGN_OP LEFT_PAREN name= NAME ( field_constriant[column] )* RIGHT_PAREN ;
    public void bound_pattern(ConditionalElementDescr in_ce) throws RecognitionException {
        Token var=null;
        Token name=null;

        
                ColumnDescr column = null;
                String identifier = null;
            
        try {
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:418:4: (var= VAR ASSIGN_OP LEFT_PAREN name= NAME ( field_constriant[column] )* RIGHT_PAREN )
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:418:4: var= VAR ASSIGN_OP LEFT_PAREN name= NAME ( field_constriant[column] )* RIGHT_PAREN
            {
            var=(Token)input.LT(1);
            match(input,VAR,FOLLOW_VAR_in_bound_pattern687); 
            
            			identifier = var.getText();
            		
            match(input,ASSIGN_OP,FOLLOW_ASSIGN_OP_in_bound_pattern693); 
            match(input,LEFT_PAREN,FOLLOW_LEFT_PAREN_in_bound_pattern695); 
            name=(Token)input.LT(1);
            match(input,NAME,FOLLOW_NAME_in_bound_pattern699); 
            
            			column = new ColumnDescr(name.getText());
            			column.setIdentifier( identifier );
            			in_ce.addDescr( column );	    
            		
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:427:3: ( field_constriant[column] )*
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);
                if ( (LA10_0==LEFT_PAREN) ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:427:3: field_constriant[column]
            	    {
            	    pushFollow(FOLLOW_field_constriant_in_bound_pattern708);
            	    field_constriant(column);
            	    _fsp--;


            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);

            match(input,RIGHT_PAREN,FOLLOW_RIGHT_PAREN_in_bound_pattern715); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end bound_pattern


    // $ANTLR start field_constriant
    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:431:1: field_constriant[ColumnDescr column] : LEFT_PAREN f= NAME connected_constraint[fc, column] RIGHT_PAREN ;
    public void field_constriant(ColumnDescr column) throws RecognitionException {
        Token f=null;

        
             	List list = new ArrayList();
        		FieldBindingDescr fbd = null;
        		FieldConstraintDescr fc = null;
        		String op = "==";
        	
        try {
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:439:3: ( LEFT_PAREN f= NAME connected_constraint[fc, column] RIGHT_PAREN )
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:439:3: LEFT_PAREN f= NAME connected_constraint[fc, column] RIGHT_PAREN
            {
            match(input,LEFT_PAREN,FOLLOW_LEFT_PAREN_in_field_constriant746); 
            f=(Token)input.LT(1);
            match(input,NAME,FOLLOW_NAME_in_field_constriant750); 
            
            			fc = new FieldConstraintDescr(f.getText());
            			fc.setLocation( offset(f.getLine()), f.getCharPositionInLine() );
            			fc.setStartCharacter( ((CommonToken)f).getStartIndex() );
            			column.addDescr( fc );			
            		
            pushFollow(FOLLOW_connected_constraint_in_field_constriant765);
            connected_constraint(fc,  column);
            _fsp--;

            match(input,RIGHT_PAREN,FOLLOW_RIGHT_PAREN_in_field_constriant771); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end field_constriant


    // $ANTLR start connected_constraint
    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:451:1: connected_constraint[FieldConstraintDescr fc, ColumnDescr column] : restriction[fc, column] ( AMPERSAND connected_constraint[fc, column] | PIPE connected_constraint[fc, column] )? ;
    public void connected_constraint(FieldConstraintDescr fc, ColumnDescr column) throws RecognitionException {
        try {
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:453:2: ( restriction[fc, column] ( AMPERSAND connected_constraint[fc, column] | PIPE connected_constraint[fc, column] )? )
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:453:2: restriction[fc, column] ( AMPERSAND connected_constraint[fc, column] | PIPE connected_constraint[fc, column] )?
            {
            pushFollow(FOLLOW_restriction_in_connected_constraint787);
            restriction(fc,  column);
            _fsp--;

            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:454:2: ( AMPERSAND connected_constraint[fc, column] | PIPE connected_constraint[fc, column] )?
            int alt11=3;
            int LA11_0 = input.LA(1);
            if ( (LA11_0==AMPERSAND) ) {
                alt11=1;
            }
            else if ( (LA11_0==PIPE) ) {
                alt11=2;
            }
            switch (alt11) {
                case 1 :
                    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:455:6: AMPERSAND connected_constraint[fc, column]
                    {
                    match(input,AMPERSAND,FOLLOW_AMPERSAND_in_connected_constraint799); 
                     fc.addRestriction(new RestrictionConnectiveDescr(RestrictionConnectiveDescr.AND)); 
                    pushFollow(FOLLOW_connected_constraint_in_connected_constraint808);
                    connected_constraint(fc,  column);
                    _fsp--;


                    }
                    break;
                case 2 :
                    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:458:6: PIPE connected_constraint[fc, column]
                    {
                    match(input,PIPE,FOLLOW_PIPE_in_connected_constraint820); 
                    fc.addRestriction(new RestrictionConnectiveDescr(RestrictionConnectiveDescr.OR)); 
                    pushFollow(FOLLOW_connected_constraint_in_connected_constraint829);
                    connected_constraint(fc,  column);
                    _fsp--;


                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end connected_constraint


    // $ANTLR start restriction
    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:463:1: restriction[FieldConstraintDescr fc, ColumnDescr column] : ( TILDE )? ( predicate_constraint[op, column] | return_value_restriction[op, fc] | variable_restriction[op, fc] | lc= literal_restriction ) ;
    public void restriction(FieldConstraintDescr fc, ColumnDescr column) throws RecognitionException {
        String lc = null;


        
        			String op = "==";
        	
        try {
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:467:4: ( ( TILDE )? ( predicate_constraint[op, column] | return_value_restriction[op, fc] | variable_restriction[op, fc] | lc= literal_restriction ) )
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:467:4: ( TILDE )? ( predicate_constraint[op, column] | return_value_restriction[op, fc] | variable_restriction[op, fc] | lc= literal_restriction )
            {
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:467:4: ( TILDE )?
            int alt12=2;
            int LA12_0 = input.LA(1);
            if ( (LA12_0==TILDE) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:467:5: TILDE
                    {
                    match(input,TILDE,FOLLOW_TILDE_in_restriction855); 
                    op = "!=";

                    }
                    break;

            }

            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:468:3: ( predicate_constraint[op, column] | return_value_restriction[op, fc] | variable_restriction[op, fc] | lc= literal_restriction )
            int alt13=4;
            switch ( input.LA(1) ) {
            case COLON:
                alt13=1;
                break;
            case EQUALS:
                alt13=2;
                break;
            case VAR:
                alt13=3;
                break;
            case NAME:
            case STRING:
            case INT:
            case FLOAT:
            case BOOL:
            case NULL:
                alt13=4;
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("468:3: ( predicate_constraint[op, column] | return_value_restriction[op, fc] | variable_restriction[op, fc] | lc= literal_restriction )", 13, 0, input);

                throw nvae;
            }

            switch (alt13) {
                case 1 :
                    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:468:5: predicate_constraint[op, column]
                    {
                    pushFollow(FOLLOW_predicate_constraint_in_restriction871);
                    predicate_constraint(op,  column);
                    _fsp--;


                    }
                    break;
                case 2 :
                    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:469:7: return_value_restriction[op, fc]
                    {
                    pushFollow(FOLLOW_return_value_restriction_in_restriction887);
                    return_value_restriction(op,  fc);
                    _fsp--;


                    }
                    break;
                case 3 :
                    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:470:7: variable_restriction[op, fc]
                    {
                    pushFollow(FOLLOW_variable_restriction_in_restriction896);
                    variable_restriction(op,  fc);
                    _fsp--;


                    }
                    break;
                case 4 :
                    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:471:8: lc= literal_restriction
                    {
                    pushFollow(FOLLOW_literal_restriction_in_restriction908);
                    lc=literal_restriction();
                    _fsp--;

                    
                         	    			fc.addRestriction( new LiteralRestrictionDescr(op, lc, true) );
                    		      		op = "==";
                    		        

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end restriction


    // $ANTLR start predicate_constraint
    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:478:1: predicate_constraint[String op, ColumnDescr column] : COLON fc= function[context] ;
    public void predicate_constraint(String op, ColumnDescr column) throws RecognitionException {
        FunctionCaller fc = null;


        
           		ExecutionEngine engine = new CLPPredicate();
        		ExecutionBuildContext context = new ExecutionBuildContext( engine );    
            
        try {
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:483:4: ( COLON fc= function[context] )
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:483:4: COLON fc= function[context]
            {
            match(input,COLON,FOLLOW_COLON_in_predicate_constraint950); 
            pushFollow(FOLLOW_function_in_predicate_constraint956);
            fc=function(context);
            _fsp--;

            	
            		    engine.addFunction( fc );
            			column.addDescr( new PredicateDescr( engine ) );
            		

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end predicate_constraint


    // $ANTLR start return_value_restriction
    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:492:1: return_value_restriction[String op, FieldConstraintDescr fc] : EQUALS func= function[context] ;
    public void return_value_restriction(String op, FieldConstraintDescr fc) throws RecognitionException {
        FunctionCaller func = null;


        
        		ExecutionEngine engine = new CLPReturnValue();
        		ExecutionBuildContext context = new ExecutionBuildContext( engine );
        	
        try {
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:497:4: ( EQUALS func= function[context] )
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:497:4: EQUALS func= function[context]
            {
            match(input,EQUALS,FOLLOW_EQUALS_in_return_value_restriction982); 
            pushFollow(FOLLOW_function_in_return_value_restriction989);
            func=function(context);
            _fsp--;

            					
               		    engine.addFunction( func );
            			fc.addRestriction( new ReturnValueRestrictionDescr (op, engine ) );
            		

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end return_value_restriction


    // $ANTLR start variable_restriction
    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:504:1: variable_restriction[String op, FieldConstraintDescr fc] : var= VAR ;
    public void variable_restriction(String op, FieldConstraintDescr fc) throws RecognitionException {
        Token var=null;

        try {
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:505:4: (var= VAR )
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:505:4: var= VAR
            {
            var=(Token)input.LT(1);
            match(input,VAR,FOLLOW_VAR_in_variable_restriction1010); 
            
            			fc.addRestriction( new VariableRestrictionDescr(op, var.getText()) );
            		

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end variable_restriction


    // $ANTLR start literal_restriction
    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:511:1: literal_restriction returns [String text] : t= literal ;
    public String literal_restriction() throws RecognitionException {
        String text = null;

        String t = null;


        
        		text = null;
        	
        try {
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:516:6: (t= literal )
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:516:6: t= literal
            {
            pushFollow(FOLLOW_literal_in_literal_restriction1043);
            t=literal();
            _fsp--;

            
            	    	text = t;
            	    

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return text;
    }
    // $ANTLR end literal_restriction


    // $ANTLR start function
    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:521:1: function[ExecutionBuildContext context] returns [FunctionCaller fc] : LEFT_PAREN name= NAME ( function_params[context, fc] )+ RIGHT_PAREN ;
    public FunctionCaller function(ExecutionBuildContext context) throws RecognitionException {
        FunctionCaller fc = null;

        Token name=null;

        
        		Function f = null;        
        	
        try {
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:525:4: ( LEFT_PAREN name= NAME ( function_params[context, fc] )+ RIGHT_PAREN )
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:525:4: LEFT_PAREN name= NAME ( function_params[context, fc] )+ RIGHT_PAREN
            {
            match(input,LEFT_PAREN,FOLLOW_LEFT_PAREN_in_function1068); 
            name=(Token)input.LT(1);
            match(input,NAME,FOLLOW_NAME_in_function1074); 
            
            			if ( name.getText().equals("bind") ) {
            		  		context.createLocalVariable( name.getText() );
            			}
            		  	f = functionRegistry.getFunction( name.getText() );		  
            			fc= new FunctionCaller( f );
            		
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:534:3: ( function_params[context, fc] )+
            int cnt14=0;
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);
                if ( (LA14_0==LEFT_PAREN||(LA14_0>=NAME && LA14_0<=STRING)||LA14_0==INT||LA14_0==VAR||(LA14_0>=FLOAT && LA14_0<=NULL)) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:534:3: function_params[context, fc]
            	    {
            	    pushFollow(FOLLOW_function_params_in_function1087);
            	    function_params(context,  fc);
            	    _fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt14 >= 1 ) break loop14;
                        EarlyExitException eee =
                            new EarlyExitException(14, input);
                        throw eee;
                }
                cnt14++;
            } while (true);

            match(input,RIGHT_PAREN,FOLLOW_RIGHT_PAREN_in_function1097); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return fc;
    }
    // $ANTLR end function


    // $ANTLR start modify_function
    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:540:1: modify_function[ExecutionBuildContext context] returns [FunctionCaller fc] : LEFT_PAREN MODIFY t= VAR ( slot_name_value_pair[context, fc] )+ RIGHT_PAREN ;
    public FunctionCaller modify_function(ExecutionBuildContext context) throws RecognitionException {
        FunctionCaller fc = null;

        Token t=null;

        try {
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:542:3: ( LEFT_PAREN MODIFY t= VAR ( slot_name_value_pair[context, fc] )+ RIGHT_PAREN )
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:542:3: LEFT_PAREN MODIFY t= VAR ( slot_name_value_pair[context, fc] )+ RIGHT_PAREN
            {
            match(input,LEFT_PAREN,FOLLOW_LEFT_PAREN_in_modify_function1121); 
            match(input,MODIFY,FOLLOW_MODIFY_in_modify_function1126); 
            
            				fc = new FunctionCaller( functionRegistry.getFunction( "modify" ) );
            			
            t=(Token)input.LT(1);
            match(input,VAR,FOLLOW_VAR_in_modify_function1135); 
             fc.addParameter( context.getVariableValueHandler( t.getText() ) ); 
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:547:4: ( slot_name_value_pair[context, fc] )+
            int cnt15=0;
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);
                if ( (LA15_0==LEFT_PAREN) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:547:4: slot_name_value_pair[context, fc]
            	    {
            	    pushFollow(FOLLOW_slot_name_value_pair_in_modify_function1146);
            	    slot_name_value_pair(context,  fc);
            	    _fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt15 >= 1 ) break loop15;
                        EarlyExitException eee =
                            new EarlyExitException(15, input);
                        throw eee;
                }
                cnt15++;
            } while (true);

            match(input,RIGHT_PAREN,FOLLOW_RIGHT_PAREN_in_modify_function1152); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return fc;
    }
    // $ANTLR end modify_function


    // $ANTLR start function_params
    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:551:1: function_params[ExecutionBuildContext context, FunctionCaller fc] : (t= VAR | t= STRING | t= NAME | t= FLOAT | t= INT | t= BOOL | t= NULL | nfc= function[context] ) ;
    public void function_params(ExecutionBuildContext context, FunctionCaller fc) throws RecognitionException {
        Token t=null;
        FunctionCaller nfc = null;


        
        		ValueHandler value  =  null;		
        	
        try {
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:556:3: ( (t= VAR | t= STRING | t= NAME | t= FLOAT | t= INT | t= BOOL | t= NULL | nfc= function[context] ) )
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:556:3: (t= VAR | t= STRING | t= NAME | t= FLOAT | t= INT | t= BOOL | t= NULL | nfc= function[context] )
            {
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:556:3: (t= VAR | t= STRING | t= NAME | t= FLOAT | t= INT | t= BOOL | t= NULL | nfc= function[context] )
            int alt16=8;
            switch ( input.LA(1) ) {
            case VAR:
                alt16=1;
                break;
            case STRING:
                alt16=2;
                break;
            case NAME:
                alt16=3;
                break;
            case FLOAT:
                alt16=4;
                break;
            case INT:
                alt16=5;
                break;
            case BOOL:
                alt16=6;
                break;
            case NULL:
                alt16=7;
                break;
            case LEFT_PAREN:
                alt16=8;
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("556:3: (t= VAR | t= STRING | t= NAME | t= FLOAT | t= INT | t= BOOL | t= NULL | nfc= function[context] )", 16, 0, input);

                throw nvae;
            }

            switch (alt16) {
                case 1 :
                    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:556:6: t= VAR
                    {
                    t=(Token)input.LT(1);
                    match(input,VAR,FOLLOW_VAR_in_function_params1181); 
                     value = context.getVariableValueHandler(t.getText() ); 

                    }
                    break;
                case 2 :
                    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:557:6: t= STRING
                    {
                    t=(Token)input.LT(1);
                    match(input,STRING,FOLLOW_STRING_in_function_params1193); 
                     value = new ObjectLiteralValue( getString( t ) ); 

                    }
                    break;
                case 3 :
                    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:558:7: t= NAME
                    {
                    t=(Token)input.LT(1);
                    match(input,NAME,FOLLOW_NAME_in_function_params1208); 
                     value = new ObjectLiteralValue( t.getText() ); 

                    }
                    break;
                case 4 :
                    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:559:6: t= FLOAT
                    {
                    t=(Token)input.LT(1);
                    match(input,FLOAT,FOLLOW_FLOAT_in_function_params1225); 
                     value = new DoubleLiteralValue( t.getText() ); 

                    }
                    break;
                case 5 :
                    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:560:6: t= INT
                    {
                    t=(Token)input.LT(1);
                    match(input,INT,FOLLOW_INT_in_function_params1240); 
                     value = new LongLiteralValue( t.getText() ); 

                    }
                    break;
                case 6 :
                    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:561:6: t= BOOL
                    {
                    t=(Token)input.LT(1);
                    match(input,BOOL,FOLLOW_BOOL_in_function_params1260); 
                     value = new BooleanLiteralValue( t.getText() ); 

                    }
                    break;
                case 7 :
                    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:562:6: t= NULL
                    {
                    t=(Token)input.LT(1);
                    match(input,NULL,FOLLOW_NULL_in_function_params1282); 
                     value = ObjectLiteralValue.NULL; 

                    }
                    break;
                case 8 :
                    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:563:6: nfc= function[context]
                    {
                    pushFollow(FOLLOW_function_in_function_params1298);
                    nfc=function(context);
                    _fsp--;

                     value = nfc; 

                    }
                    break;

            }

             fc.addParameter( value ); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end function_params


    // $ANTLR start slot_name_value_pair
    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:569:1: slot_name_value_pair[ExecutionBuildContext context, FunctionCaller fc] : LEFT_PAREN id= NAME (t= VAR | t= STRING | t= NAME | t= FLOAT | t= INT | t= BOOL | t= NULL | nf= function[context] ) RIGHT_PAREN ;
    public void slot_name_value_pair(ExecutionBuildContext context, FunctionCaller fc) throws RecognitionException {
        Token id=null;
        Token t=null;
        FunctionCaller nf = null;


        
        		SlotNameValuePair nameValuePair = null;
        		String name = null;
        	
        try {
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:575:3: ( LEFT_PAREN id= NAME (t= VAR | t= STRING | t= NAME | t= FLOAT | t= INT | t= BOOL | t= NULL | nf= function[context] ) RIGHT_PAREN )
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:575:3: LEFT_PAREN id= NAME (t= VAR | t= STRING | t= NAME | t= FLOAT | t= INT | t= BOOL | t= NULL | nf= function[context] ) RIGHT_PAREN
            {
            match(input,LEFT_PAREN,FOLLOW_LEFT_PAREN_in_slot_name_value_pair1339); 
            id=(Token)input.LT(1);
            match(input,NAME,FOLLOW_NAME_in_slot_name_value_pair1345); 
            
            			name = id.getText();
            		
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:579:3: (t= VAR | t= STRING | t= NAME | t= FLOAT | t= INT | t= BOOL | t= NULL | nf= function[context] )
            int alt17=8;
            switch ( input.LA(1) ) {
            case VAR:
                alt17=1;
                break;
            case STRING:
                alt17=2;
                break;
            case NAME:
                alt17=3;
                break;
            case FLOAT:
                alt17=4;
                break;
            case INT:
                alt17=5;
                break;
            case BOOL:
                alt17=6;
                break;
            case NULL:
                alt17=7;
                break;
            case LEFT_PAREN:
                alt17=8;
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("579:3: (t= VAR | t= STRING | t= NAME | t= FLOAT | t= INT | t= BOOL | t= NULL | nf= function[context] )", 17, 0, input);

                throw nvae;
            }

            switch (alt17) {
                case 1 :
                    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:579:6: t= VAR
                    {
                    t=(Token)input.LT(1);
                    match(input,VAR,FOLLOW_VAR_in_slot_name_value_pair1356); 
                     nameValuePair = new SlotNameValuePair(name, context.getVariableValueHandler( t.getText() ) ); 

                    }
                    break;
                case 2 :
                    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:580:7: t= STRING
                    {
                    t=(Token)input.LT(1);
                    match(input,STRING,FOLLOW_STRING_in_slot_name_value_pair1374); 
                     nameValuePair = new SlotNameValuePair(name, new ObjectLiteralValue( getString( t ) ) ); 

                    }
                    break;
                case 3 :
                    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:581:7: t= NAME
                    {
                    t=(Token)input.LT(1);
                    match(input,NAME,FOLLOW_NAME_in_slot_name_value_pair1389); 
                     nameValuePair = new SlotNameValuePair(name, new ObjectLiteralValue( t.getText() ) ); 

                    }
                    break;
                case 4 :
                    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:582:6: t= FLOAT
                    {
                    t=(Token)input.LT(1);
                    match(input,FLOAT,FOLLOW_FLOAT_in_slot_name_value_pair1406); 
                     nameValuePair = new SlotNameValuePair(name, new DoubleLiteralValue( t.getText() ) ); 

                    }
                    break;
                case 5 :
                    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:583:6: t= INT
                    {
                    t=(Token)input.LT(1);
                    match(input,INT,FOLLOW_INT_in_slot_name_value_pair1421); 
                     nameValuePair = new SlotNameValuePair(name, new LongLiteralValue( t.getText() ) ); 

                    }
                    break;
                case 6 :
                    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:584:6: t= BOOL
                    {
                    t=(Token)input.LT(1);
                    match(input,BOOL,FOLLOW_BOOL_in_slot_name_value_pair1441); 
                     nameValuePair = new SlotNameValuePair(name, new BooleanLiteralValue( t.getText() ) ) ; 

                    }
                    break;
                case 7 :
                    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:585:6: t= NULL
                    {
                    t=(Token)input.LT(1);
                    match(input,NULL,FOLLOW_NULL_in_slot_name_value_pair1463); 
                     nameValuePair = new SlotNameValuePair(name, ObjectLiteralValue.NULL ); 

                    }
                    break;
                case 8 :
                    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:586:6: nf= function[context]
                    {
                    pushFollow(FOLLOW_function_in_slot_name_value_pair1479);
                    nf=function(context);
                    _fsp--;

                     nameValuePair = new SlotNameValuePair(name, nf ); 

                    }
                    break;

            }

             fc.addParameter( nameValuePair ); 
            match(input,RIGHT_PAREN,FOLLOW_RIGHT_PAREN_in_slot_name_value_pair1516); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end slot_name_value_pair


    // $ANTLR start literal
    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:593:1: literal returns [String text] : (t= STRING | t= NAME | t= INT | t= FLOAT | t= BOOL | t= NULL ) ;
    public String literal() throws RecognitionException {
        String text = null;

        Token t=null;

        
        		text = null;
        	
        try {
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:597:4: ( (t= STRING | t= NAME | t= INT | t= FLOAT | t= BOOL | t= NULL ) )
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:597:4: (t= STRING | t= NAME | t= INT | t= FLOAT | t= BOOL | t= NULL )
            {
            // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:597:4: (t= STRING | t= NAME | t= INT | t= FLOAT | t= BOOL | t= NULL )
            int alt18=6;
            switch ( input.LA(1) ) {
            case STRING:
                alt18=1;
                break;
            case NAME:
                alt18=2;
                break;
            case INT:
                alt18=3;
                break;
            case FLOAT:
                alt18=4;
                break;
            case BOOL:
                alt18=5;
                break;
            case NULL:
                alt18=6;
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("597:4: (t= STRING | t= NAME | t= INT | t= FLOAT | t= BOOL | t= NULL )", 18, 0, input);

                throw nvae;
            }

            switch (alt18) {
                case 1 :
                    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:597:8: t= STRING
                    {
                    t=(Token)input.LT(1);
                    match(input,STRING,FOLLOW_STRING_in_literal1545); 
                     text = getString( t ); 

                    }
                    break;
                case 2 :
                    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:598:7: t= NAME
                    {
                    t=(Token)input.LT(1);
                    match(input,NAME,FOLLOW_NAME_in_literal1558); 
                     text = t.getText(); 

                    }
                    break;
                case 3 :
                    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:599:7: t= INT
                    {
                    t=(Token)input.LT(1);
                    match(input,INT,FOLLOW_INT_in_literal1574); 
                     text = t.getText(); 

                    }
                    break;
                case 4 :
                    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:600:7: t= FLOAT
                    {
                    t=(Token)input.LT(1);
                    match(input,FLOAT,FOLLOW_FLOAT_in_literal1589); 
                     text = t.getText(); 

                    }
                    break;
                case 5 :
                    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:601:7: t= BOOL
                    {
                    t=(Token)input.LT(1);
                    match(input,BOOL,FOLLOW_BOOL_in_literal1602); 
                     text = t.getText(); 

                    }
                    break;
                case 6 :
                    // D:\\workspace\\jboss\\jbossrules\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:602:7: t= NULL
                    {
                    t=(Token)input.LT(1);
                    match(input,NULL,FOLLOW_NULL_in_literal1616); 
                     text = null; 

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return text;
    }
    // $ANTLR end literal


 

    public static final BitSet FOLLOW_LEFT_PAREN_in_defrule68 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_DEFRULE_in_defrule76 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_NAME_in_defrule80 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_STRING_in_defrule92 = new BitSet(new long[]{0x0000040000010010L});
    public static final BitSet FOLLOW_ruleAttribute_in_defrule98 = new BitSet(new long[]{0x0000040000010010L});
    public static final BitSet FOLLOW_ce_in_defrule106 = new BitSet(new long[]{0x0000040000010010L});
    public static final BitSet FOLLOW_42_in_defrule115 = new BitSet(new long[]{0x0000000000000110L});
    public static final BitSet FOLLOW_rhs_in_defrule124 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_RIGHT_PAREN_in_defrule133 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFT_PAREN_in_ruleAttribute150 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_DECLARE_in_ruleAttribute152 = new BitSet(new long[]{0x0000000000000110L});
    public static final BitSet FOLLOW_LEFT_PAREN_in_ruleAttribute159 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_salience_in_ruleAttribute163 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_RIGHT_PAREN_in_ruleAttribute167 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_RIGHT_PAREN_in_ruleAttribute174 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SALIENCE_in_salience204 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_INT_in_salience208 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_and_ce_in_ce234 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_or_ce_in_ce244 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_not_ce_in_ce253 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_exists_ce_in_ce262 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_eval_ce_in_ce276 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normal_pattern_in_ce290 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_bound_pattern_in_ce299 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modify_function_in_rhs343 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_function_in_rhs356 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_LEFT_PAREN_in_and_ce393 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_AND_in_and_ce398 = new BitSet(new long[]{0x0000000000010010L});
    public static final BitSet FOLLOW_ce_in_and_ce404 = new BitSet(new long[]{0x0000000000010110L});
    public static final BitSet FOLLOW_RIGHT_PAREN_in_and_ce413 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFT_PAREN_in_or_ce441 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_OR_in_or_ce446 = new BitSet(new long[]{0x0000000000010010L});
    public static final BitSet FOLLOW_ce_in_or_ce452 = new BitSet(new long[]{0x0000000000010110L});
    public static final BitSet FOLLOW_RIGHT_PAREN_in_or_ce461 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFT_PAREN_in_not_ce489 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_NOT_in_not_ce494 = new BitSet(new long[]{0x0000000000010010L});
    public static final BitSet FOLLOW_ce_in_not_ce500 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_RIGHT_PAREN_in_not_ce508 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFT_PAREN_in_exists_ce537 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_EXISTS_in_exists_ce542 = new BitSet(new long[]{0x0000000000010010L});
    public static final BitSet FOLLOW_ce_in_exists_ce548 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_RIGHT_PAREN_in_exists_ce556 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFT_PAREN_in_eval_ce584 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_TEST_in_eval_ce589 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_function_in_eval_ce597 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_RIGHT_PAREN_in_eval_ce608 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFT_PAREN_in_normal_pattern636 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_NAME_in_normal_pattern643 = new BitSet(new long[]{0x0000000000000110L});
    public static final BitSet FOLLOW_field_constriant_in_normal_pattern649 = new BitSet(new long[]{0x0000000000000110L});
    public static final BitSet FOLLOW_RIGHT_PAREN_in_normal_pattern659 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_bound_pattern687 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_ASSIGN_OP_in_bound_pattern693 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_LEFT_PAREN_in_bound_pattern695 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_NAME_in_bound_pattern699 = new BitSet(new long[]{0x0000000000000110L});
    public static final BitSet FOLLOW_field_constriant_in_bound_pattern708 = new BitSet(new long[]{0x0000000000000110L});
    public static final BitSet FOLLOW_RIGHT_PAREN_in_bound_pattern715 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFT_PAREN_in_field_constriant746 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_NAME_in_field_constriant750 = new BitSet(new long[]{0x00000000077104C0L});
    public static final BitSet FOLLOW_connected_constraint_in_field_constriant765 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_RIGHT_PAREN_in_field_constriant771 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_restriction_in_connected_constraint787 = new BitSet(new long[]{0x00000000000C0002L});
    public static final BitSet FOLLOW_AMPERSAND_in_connected_constraint799 = new BitSet(new long[]{0x00000000077104C0L});
    public static final BitSet FOLLOW_connected_constraint_in_connected_constraint808 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PIPE_in_connected_constraint820 = new BitSet(new long[]{0x00000000077104C0L});
    public static final BitSet FOLLOW_connected_constraint_in_connected_constraint829 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TILDE_in_restriction855 = new BitSet(new long[]{0x00000000076104C0L});
    public static final BitSet FOLLOW_predicate_constraint_in_restriction871 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_return_value_restriction_in_restriction887 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variable_restriction_in_restriction896 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literal_restriction_in_restriction908 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COLON_in_predicate_constraint950 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_function_in_predicate_constraint956 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EQUALS_in_return_value_restriction982 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_function_in_return_value_restriction989 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_variable_restriction1010 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literal_in_literal_restriction1043 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFT_PAREN_in_function1068 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_NAME_in_function1074 = new BitSet(new long[]{0x00000000070104D0L});
    public static final BitSet FOLLOW_function_params_in_function1087 = new BitSet(new long[]{0x00000000070105D0L});
    public static final BitSet FOLLOW_RIGHT_PAREN_in_function1097 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFT_PAREN_in_modify_function1121 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_MODIFY_in_modify_function1126 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_VAR_in_modify_function1135 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_slot_name_value_pair_in_modify_function1146 = new BitSet(new long[]{0x0000000000000110L});
    public static final BitSet FOLLOW_RIGHT_PAREN_in_modify_function1152 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_function_params1181 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_function_params1193 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_function_params1208 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FLOAT_in_function_params1225 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_function_params1240 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BOOL_in_function_params1260 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NULL_in_function_params1282 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_function_in_function_params1298 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFT_PAREN_in_slot_name_value_pair1339 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_NAME_in_slot_name_value_pair1345 = new BitSet(new long[]{0x00000000070104D0L});
    public static final BitSet FOLLOW_VAR_in_slot_name_value_pair1356 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_STRING_in_slot_name_value_pair1374 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_NAME_in_slot_name_value_pair1389 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_FLOAT_in_slot_name_value_pair1406 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_INT_in_slot_name_value_pair1421 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_BOOL_in_slot_name_value_pair1441 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_NULL_in_slot_name_value_pair1463 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_function_in_slot_name_value_pair1479 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_RIGHT_PAREN_in_slot_name_value_pair1516 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_literal1545 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_literal1558 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_literal1574 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FLOAT_in_literal1589 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BOOL_in_literal1602 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NULL_in_literal1616 = new BitSet(new long[]{0x0000000000000002L});

}