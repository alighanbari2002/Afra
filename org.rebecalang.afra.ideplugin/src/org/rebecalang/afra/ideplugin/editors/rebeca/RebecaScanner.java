package org.rebecalang.afra.ideplugin.editors.rebeca;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;
import org.rebecalang.afra.ideplugin.editors.ColorManager;

public class RebecaScanner extends RuleBasedScanner {
	// Language keywords (keep purple) - core language constructs
	private static final String[] rebecaKeywords = {"reactiveclass", "if", "else",
			"msgsrv", "knownrebecs", "statevars", "main", "self", "sender",
			"externalclass", "sends", "of", "globalvariables"};
	
	// Data types (dark blue)
	private static final String[] rebecaTypes = {"boolean", "byte", "short", "int", "bitint"};
	
	// Built-in functions and literals (medium blue)
	private static final String[] rebecaBuiltins = {"pow", "after", "deadline", "delay", "true", "false"};
	
	// Common variable names found in Rebeca code (dark goldenrod)
	private static final String[] commonVariables = {
		// From DiningPhilosophers sample
		"eating", "cL", "cR", "chpL", "chpR", "philL", "philR", 
		"lAssign", "rAssign", "leftReq", "rightReq",
		// From LeaderElection sample  
		"id", "phase", "oneResponseIsAlreadyReceived", "leaderId", "nodeL", "nodeR",
		"msgId", "out", "hopCount", "selectedLeaderId", "myId",
		// From TicketService sample
		"sent", "issueDelay", "customer", "myIssueDelay", "myId",
		// Common parameter and variable names
		"sender", "i", "j", "count", "temp", "result", "value", "data", "state"
	};

	public RebecaScanner(ColorManager manager)
	{
		System.out.println("[Rebeca Scanner] Initializing enhanced syntax highlighting with variable detection...");
		
		// Create tokens for different categories
		IToken keywordToken = new Token(RebecaTextAttribute.KEY_WORD.getTextAttribute(manager));
		IToken typeToken = new Token(RebecaTextAttribute.TYPE.getTextAttribute(manager));
		IToken builtinToken = new Token(RebecaTextAttribute.BUILTIN_FUNCTION.getTextAttribute(manager));
		IToken variableToken = new Token(RebecaTextAttribute.VARIABLE.getTextAttribute(manager));
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
			System.out.println("[Rebeca Scanner] Added keyword: " + keyword);
		}
		
		// Add types (dark blue)
		for (String type : rebecaTypes) {
			wordRule.addWord(type, typeToken);
			System.out.println("[Rebeca Scanner] Added type: " + type);
		}
		
		// Add built-in functions and literals (medium blue)
		for (String builtin : rebecaBuiltins) {
			wordRule.addWord(builtin, builtinToken);
			System.out.println("[Rebeca Scanner] Added built-in: " + builtin);
		}
		
		// Add common variables (dark goldenrod)
		int variableCount = 0;
		for (String variable : commonVariables) {
			wordRule.addWord(variable, variableToken);
			variableCount++;
			if (variableCount <= 10) { // Only log first 10 to reduce console noise
				System.out.println("[Rebeca Scanner] Added variable: " + variable);
			}
		}
		if (variableCount > 10) {
			System.out.println("[Rebeca Scanner] ... and " + (variableCount - 10) + " more variables");
		}
		
		rules.add(wordRule);

		IRule[] result = new IRule[rules.size()];
		rules.toArray(result);
		setRules(result);
		
		System.out.println("[Rebeca Scanner] Enhanced scanner initialized with " + rules.size() + " rules");
		System.out.println("[Rebeca Scanner] Keywords: " + rebecaKeywords.length + " (purple)");
		System.out.println("[Rebeca Scanner] Types: " + rebecaTypes.length + " (dark blue)");
		System.out.println("[Rebeca Scanner] Built-ins: " + rebecaBuiltins.length + " (medium blue)");
		System.out.println("[Rebeca Scanner] Variables: " + commonVariables.length + " (dark goldenrod)");
		System.out.println("[Rebeca Scanner] Variable detection active for identifiers like: eating, id, sender, etc.");
	}
}
