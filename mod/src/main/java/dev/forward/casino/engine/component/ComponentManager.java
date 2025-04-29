package dev.forward.casino.engine.component;

import com.google.gson.Gson;
import dev.forward.casino.engine.elements.AbstractElement;
import dev.forward.casino.engine.elements.Input;
import dev.forward.casino.engine.util.input.TabComplete;
import dev.forward.casino.util.network.ModTransfer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ComponentManager {
    private final Map<Class<? extends ComponentEvent>, Set<AbstractElement<?>>> componentEventsMap = new HashMap<>();
    protected final HashMap<String, Input> inputMap = new HashMap<>();
    protected final Gson gson = new Gson();

    public ComponentManager() {
        this.registerResponseHandler();
    }

    private void registerResponseHandler() {
        ModTransfer.registerChannel("tb|response", 100, data -> {
            if (data == null) {
                return;
            }
            String jsonString = data.readJson();
            TabComplete tabCompleteData = (TabComplete)this.gson.fromJson(jsonString, TabComplete.class);
            Input input = this.inputMap.get(tabCompleteData.getId());
            if (input == null) {
                return;
            }
            this.updateInputWithTabComplete(input, tabCompleteData);
        });
    }

    public void updateInputWithTabComplete(Input input, TabComplete tabCompleteData) {
        input.setSelectedTabIndex(0).setAvailableTabCompletions(tabCompleteData.getOptions());
        if (tabCompleteData.getOptions().isEmpty()) {
            input.setTabCompleteText("");
            return;
        }
        String firstOption = tabCompleteData.getOptions().get(0);
        if (!input.getPlaceHolder().equals(firstOption)) {
            input.setTabCompleteText(firstOption);
        }
        input.setTabCompleteText(firstOption);
        this.syncTextWithTabCompletion(input);
    }

    public void syncTextWithTabCompletion(Input input) {
        String tabCompleteText = input.getCurrentTabCompletion();
        String currentText = input.getText();
        if (tabCompleteText != null && currentText != null) {
            StringBuilder updatedText = new StringBuilder(currentText);
            char[] tabCompleteChars = tabCompleteText.toCharArray();
            for (int i = 0; i < Math.min(tabCompleteChars.length, updatedText.length()); ++i) {
                char tabChar = tabCompleteChars[i];
                char textChar = updatedText.charAt(i);
                if (Character.toLowerCase(tabChar) != Character.toLowerCase(textChar) || tabChar == textChar) continue;
                updatedText.setCharAt(i, tabChar);
            }
            input.setText(updatedText.toString());
        }
    }

    public <B extends ComponentEvent> void fireComponentEvent(B event) {
        Set<AbstractElement<?>> elements = this.componentEventsMap.get(event.getClass());
        if (elements != null && !elements.isEmpty()) {
            for(AbstractElement<?> element : elements) {
                event.setElement(element);
                element.fireEvent(event);
            }

        }
    }

    public <B extends ComponentEvent> void registerComponentEvent(Class<B> eventClass, AbstractElement<?> element) {
        Set<AbstractElement<?>> elementsForEvent = this.componentEventsMap.computeIfAbsent(eventClass, k -> new HashSet<>());
        elementsForEvent.add(element);
    }

    public void removeComponentEvent(Class<? extends ComponentEvent> eventClass, AbstractElement<?> element) {
        Set<AbstractElement<?>> elementsForEvent = this.componentEventsMap.get(eventClass);
        if (elementsForEvent != null) {
            elementsForEvent.remove(element);
            if (elementsForEvent.isEmpty()) {
                this.componentEventsMap.remove(eventClass);
            }
        }
    }

    public void removeAllComponentEventsForElement(AbstractElement<?> element) {
        this.componentEventsMap.entrySet().removeIf(entry -> {
            Set<AbstractElement<?>> elementsForEvent = entry.getValue();
            elementsForEvent.remove(element);
            return elementsForEvent.isEmpty();
        });
    }

    public void addInputTabComplete(String id, Input input) {
        if (id == null || id.isEmpty()) {
            return;
        }
        if (input == null) {
            return;
        }
        this.inputMap.put(id, input);
    }

    public void removeInputTabCompleteById(String id) {
        if (id == null || id.isEmpty()) {
            return;
        }
        this.inputMap.remove(id);
    }

    public void unload() {
        this.componentEventsMap.clear();
        this.inputMap.clear();
    }
}
