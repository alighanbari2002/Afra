package org.rebecalang.afra.ideplugin.editors.rebecaprop;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;
import org.rebecalang.afra.ideplugin.editors.ColorManager;

public class RebecaPropScanner extends RuleBasedScanner {
	// Property file keywords (purple)
	private static final String[] rebecaPropKeywords = {"property", "define", "CTL", "LTL", "Assertion"};
	
	// Temporal logic operators (medium blue)
	private static final String[] temporalOperators = {"G", "F", "X", "U", "R"};
	
	// Boolean literals (medium blue) 
	private static final String[] booleanLiterals = {"true", "false"};

	public RebecaPropScanner(ColorManager manager)
	{
		System.out.println("[Property Scanner] Initializing enhanced property file syntax highlighting...");
		
		IToken keyword = new Token(RebecaPropTextAttribute.KEYWORD.getTextAttribute(manager));
		IToken temporal = new Token(RebecaPropTextAttribute.KEYWORD.getTextAttribute(manager)); // Use same as keywords for now
		IToken literal = new Token(RebecaPropTextAttribute.KEYWORD.getTextAttribute(manager)); // Use same as keywords for now
		IToken other = new Token(RebecaPropTextAttribute.DEFAULT.getTextAttribute(manager));
	
		List<WordRule> rules = new ArrayList<WordRule>();
	
		WordRule wordRule = new WordRule(new IWordDetector()
		{
			public boolean isWordPart(char character)
			{
				return Character.isJavaIdentifierPart(character);
			}
			public boolean isWordStart(char character)
			{
				return Character.isJavaIdentifierStart(character);
			}
		}, other);
	
		// Add property keywords
		for (String propKeyword : rebecaPropKeywords) {
			wordRule.addWord(propKeyword, keyword);
		}
		System.out.println("[Property Scanner] Added " + rebecaPropKeywords.length + " property keywords");
		
		// Add temporal logic operators
		for (String operator : temporalOperators) {
			wordRule.addWord(operator, temporal);
		}
		System.out.println("[Property Scanner] Added " + temporalOperators.length + " temporal operators");
		
		// Add boolean literals
		for (String literal_word : booleanLiterals) {
			wordRule.addWord(literal_word, literal);
		}
		System.out.println("[Property Scanner] Added " + booleanLiterals.length + " boolean literals");
		
		rules.add(wordRule);
	
		IRule[] result = new IRule[rules.size()];
		rules.toArray(result);
		setRules(result);
		
		System.out.println("[Property Scanner] Enhanced property scanner initialized with " + rules.size() + " rules");
	}
}
