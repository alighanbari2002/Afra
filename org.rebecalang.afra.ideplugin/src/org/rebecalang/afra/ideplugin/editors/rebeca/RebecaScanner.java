package org.rebecalang.afra.ideplugin.editors.rebeca;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;
import org.rebecalang.afra.ideplugin.editors.ColorManager;

public class RebecaScanner extends RuleBasedScanner {
	// Language keywords (keep purple) - core language constructs
	private static final String[] rebecaKeywords = {"reactiveclass", "if", "else", "for", "while", "break",
			"msgsrv", "knownrebecs", "statevars", "main", "self", "sender", "env",
			"externalclass", "sends", "of", "globalvariables"};
	
	// Data types (dark blue)
	private static final String[] rebecaTypes = {"boolean", "byte", "short", "int", "bitint"};
	
	// Boolean literals (medium blue)
	private static final String[] rebecaLiterals = {"true", "false"};
	
	// Language constructs (keep purple with keywords)
	private static final String[] rebecaConstructs = {"after", "deadline", "delay"};

	public RebecaScanner(ColorManager manager)
	{
		System.out.println("[Rebeca Scanner] Initializing enhanced syntax highlighting (core language elements only)...");
		
		// Create tokens for different categories
		IToken keywordToken = new Token(RebecaTextAttribute.KEY_WORD.getTextAttribute(manager));
		IToken typeToken = new Token(RebecaTextAttribute.TYPE.getTextAttribute(manager));
		IToken numberToken = new Token(RebecaTextAttribute.NUMBER.getTextAttribute(manager));
		IToken builtinToken = new Token(RebecaTextAttribute.BUILTIN_FUNCTION.getTextAttribute(manager));
		IToken defaultToken = new Token(RebecaTextAttribute.DEFAULT.getTextAttribute(manager));

		List<WordRule> rules = new ArrayList<WordRule>();

		// Main word rule for identifiers
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
		}, defaultToken);

		// Add keywords (keep purple and bold)
		for (String keyword : rebecaKeywords) {
			wordRule.addWord(keyword, keywordToken);
		}
		System.out.println("[Rebeca Scanner] Added " + rebecaKeywords.length + " keywords (purple)");
		
		// Add language constructs (purple with keywords)
		for (String construct : rebecaConstructs) {
			wordRule.addWord(construct, keywordToken);
		}
		System.out.println("[Rebeca Scanner] Added " + rebecaConstructs.length + " language constructs (purple)");
		
		// Add types (dark blue)
		for (String type : rebecaTypes) {
			wordRule.addWord(type, typeToken);
		}
		System.out.println("[Rebeca Scanner] Added " + rebecaTypes.length + " types (dark blue)");
		
		// Add boolean literals (medium blue)
		for (String literal : rebecaLiterals) {
			wordRule.addWord(literal, builtinToken);
		}
		System.out.println("[Rebeca Scanner] Added " + rebecaLiterals.length + " boolean literals (medium blue)");
		
		rules.add(wordRule);

		// Add number rule for numeric literals (orange)
		WordRule numberRule = new WordRule(new NumberDetector(), numberToken);
		rules.add(numberRule);
		System.out.println("[Rebeca Scanner] Added number highlighting (orange)");

		// Note: Pattern-based detection for class names, method names, and variables
		// would require implementing custom IPredicateRule with ICharacterScanner
		// to look at context. Eclipse's WordRule framework is limited to simple keyword matching.
		// 
		// For proper implementation, we would need:
		// 1. ClassNameAfterKeywordRule - detects identifiers after "reactiveclass"
		// 2. MethodNameAfterKeywordRule - detects identifiers after "msgsrv" 
		// 3. VariableDeclarationRule - detects identifiers in "statevars" blocks
		// 4. MethodCallRule - detects method calls with "."
		//
		// This requires more sophisticated parsing than the current framework supports.
		
		System.out.println("[Rebeca Scanner] Pattern-based detection: Requires advanced parsing framework");
		System.out.println("[Rebeca Scanner] Current implementation: Keywords, types, literals, and numbers only");

		IRule[] result = new IRule[rules.size()];
		rules.toArray(result);
		setRules(result);
		
		System.out.println("[Rebeca Scanner] Enhanced scanner initialized with " + rules.size() + " rules");
		System.out.println("[Rebeca Scanner] Active language elements: " + 
			(rebecaKeywords.length + rebecaConstructs.length + rebecaTypes.length + rebecaLiterals.length) + " tokens");
		System.out.println("[Rebeca Scanner] Hardcoded arrays removed - ready for pattern-based detection implementation");
	}
	
	// Custom detector for numbers
	private static class NumberDetector implements IWordDetector {
		public boolean isWordStart(char c) {
			return Character.isDigit(c);
		}
		
		public boolean isWordPart(char c) {
			return Character.isDigit(c) || c == '.';
		}
	}
	
	/* 
	 * DESIGN NOTE: Advanced Pattern Detection
	 * 
	 * To implement proper pattern-based detection for Rebeca language elements,
	 * we would need to create custom rules that implement IPredicateRule and use
	 * ICharacterScanner for context-aware parsing:
	 * 
	 * 1. Class Name Detection:
	 *    - Look for "reactiveclass" keyword
	 *    - Skip whitespace and comments
	 *    - Capture next identifier as class name
	 *    - Example: "reactiveclass BridgeController(5) {" -> "BridgeController" is class
	 * 
	 * 2. Method Name Detection:
	 *    - Look for "msgsrv" keyword  
	 *    - Skip whitespace
	 *    - Capture next identifier as method name
	 *    - Example: "msgsrv Arrive() {" -> "Arrive" is method
	 * 
	 * 3. Variable Declaration Detection:
	 *    - Look for type keywords (boolean, int, etc.)
	 *    - Capture following identifiers as variables
	 *    - Example: "boolean eating;" -> "eating" is variable
	 * 
	 * 4. Method Call Detection:
	 *    - Look for patterns like "identifier.identifier("
	 *    - Example: "chpL.request();" -> "request" is method call
	 * 
	 * This requires implementing custom ICharacterScanner logic which is beyond
	 * the scope of simple WordRule-based highlighting.
	 */
}
