# Word Highlighting Implementation for Rebeca IDE Plugin

## Overview

This implementation adds word highlighting functionality to the Rebeca Eclipse IDE plugin. When a user clicks on a word (variable, class name, method name, etc.), all occurrences of that word in the same file are highlighted with a gray background.

## Features

- **Smart Word Detection**: Recognizes words while handling various separators including dots (`.`), commas (`,`), parentheses (`()`), braces (`{}`), and other punctuation
- **Keyword Exclusion**: Excludes Rebeca language keywords, types, and built-ins from highlighting
- **Both File Types**: Works for both `.rebeca` and `.property` files
- **Visual Feedback**: Highlights matching words with a light gray background (RGB: 220,220,220)
- **Click-to-Highlight**: Activates on mouse click events
- **Automatic Cleanup**: Clears previous highlights when clicking on a new word or outside text

## Implementation Details

### Core Components

#### 1. WordHighlightingUtil.java

Utility class that provides word detection and pattern matching functionality:

- **Word Pattern**: Uses regex `[a-zA-Z_][a-zA-Z0-9_]*` to match valid identifiers
- **Separator Handling**: Recognizes dots, commas, parentheses, braces, operators as word separators
- **Keyword Filtering**: Excludes predefined keywords from highlighting:
  - Keywords: `reactiveclass`, `if`, `else`, `msgsrv`, `knownrebecs`, `statevars`, `main`, `self`, `sender`, `externalclass`, `sends`, `of`, `globalvariables`
  - Types: `boolean`, `byte`, `short`, `int`, `bitint`
  - Built-ins: `pow`, `after`, `deadline`, `delay`, `true`, `false`

#### 2. WordHighlightManager.java

Manages the highlighting annotations in the editor:

- **Annotation Management**: Creates and manages Eclipse annotations for highlighting
- **Word Tracking**: Keeps track of currently highlighted word and its positions
- **Cleanup**: Handles removal of previous highlights when new words are selected

#### 3. Editor Integration

Both `RebecaEditor.java` and `RebecaPropEditor.java` have been updated with:

- **Mouse Listeners**: Added mouse click handlers to detect user clicks
- **Coordinate Translation**: Converts mouse coordinates to document offsets
- **Manager Integration**: Initializes and uses WordHighlightManager
- **Resource Cleanup**: Properly disposes of highlighting resources

#### 4. Plugin Configuration

Updated `plugin.xml` to register:

- **Annotation Type**: `org.rebecalang.afra.wordHighlight`
- **Visual Style**: Light gray background highlighting
- **Preference Integration**: Configurable through Eclipse preferences

## Technical Implementation

### Word Detection Algorithm

1. **Click Detection**: Mouse click events are captured by the editor
2. **Offset Calculation**: Mouse coordinates are converted to document character offset
3. **Word Boundary Detection**: Algorithm finds word start/end positions around the click point
4. **Pattern Matching**: Uses regex to find all occurrences of the selected word
5. **Annotation Creation**: Creates highlighting annotations for each occurrence
6. **Visual Update**: Editor displays the highlights with gray background

### Separator Recognition

The implementation recognizes these separators for robust word detection:

- **Whitespace**: spaces, tabs, newlines
- **Punctuation**: `.`, `,`, `;`, `:`, `!`, `?`
- **Brackets**: `()`, `{}`, `[]`, `<>`
- **Operators**: `+`, `-`, `*`, `/`, `%`, `=`, `&`, `|`, `^`, `~`

### Example Usage

```rebeca
reactiveclass Train(3) {
    knownrebecs {
        BridgeController controller;  // clicking "controller" highlights both occurrences
    }

    msgsrv YouMayPass() {
        controller.Leave();  // "controller" and "Leave" are separate words
    }
}
```

## Installation & Usage

1. **Build**: The implementation integrates into the existing Eclipse plugin build
2. **No Configuration Required**: Works automatically when the plugin is installed
3. **Usage**: Simply click on any word in `.rebeca` or `.property` files
4. **Customization**: Highlight color can be customized through Eclipse preferences

## Benefits

- **Improved Code Navigation**: Quickly identify all uses of variables, methods, and classes
- **Enhanced Readability**: Visual feedback helps understand code structure
- **Language-Aware**: Respects Rebeca syntax and doesn't highlight keywords
- **Non-Intrusive**: Only highlights on user interaction, doesn't interfere with normal editing

## Future Enhancements

Potential improvements could include:

- **Cross-File References**: Highlighting across multiple files in the same project
- **Scope-Aware Highlighting**: Different colors for different types (variables, methods, classes)
- **Quick Navigation**: Jump to next/previous occurrence functionality
- **Customizable Colors**: More color options for different word types
